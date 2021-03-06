package hu.alextoth.injector.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.annotation.Value;
import hu.alextoth.injector.core.helper.AnnotationProcessorHelper;
import hu.alextoth.injector.core.helper.DependencySorter;
import hu.alextoth.injector.exception.AnnotationProcessingException;

/**
 * Class for processing {@link Configuration}, {@link Injectable},
 * {@link Component} and {@link Inject} annotations and their alternatives.
 * 
 * @author Alex Toth
 */
public class AnnotationProcessor {

	private final AnnotationProcessorHelper annotationProcessorHelper;
	private final DependencyHandler dependencyHandler;
	private final DependencyAliasResolver dependencyAliasResolver;
	private final DependencySorter dependencySorter;
	private final ValueResolver valueResolver;

	public AnnotationProcessor(AnnotationProcessorHelper annotationProcessorHelper, DependencyHandler dependencyHandler,
			DependencyAliasResolver dependencyAliasResolver, ValueResolver valueResolver) {
		this.annotationProcessorHelper = annotationProcessorHelper;
		this.dependencyHandler = dependencyHandler;
		this.dependencyAliasResolver = dependencyAliasResolver;
		this.valueResolver = valueResolver;

		dependencySorter = new DependencySorter();
	}

	/**
	 * Method for starting the processing of {@link Configuration},
	 * {@link Injectable}, {@link Component} and {@link Inject} annotations and
	 * their alternatives.
	 */
	public void processAnnotations() {
		processConfigurationsAndInjectables();
		processComponentsAndInjections();
		processValues();
	}

	/**
	 * It processes classes annotated with {@link Configuration} and methods
	 * annotated with {@link Injectable}.<br>
	 * Instantiates configuration classes and invokes its annotated methods, then
	 * registers their return value as a resolved dependency.<br>
	 * Empty configuration classes don't get instantiated at all as they don't have
	 * any purpose.
	 */
	private void processConfigurationsAndInjectables() {
		List<Method> sortedInjectableMethods = dependencySorter
				.getSortedInjectableMethods(annotationProcessorHelper.getInjectableMethods());

		for (Method method : sortedInjectableMethods) {
			Object configurationInstance = null;
			if (!Modifier.isStatic(method.getModifiers())) {
				configurationInstance = dependencyHandler.getInstanceOf(method.getDeclaringClass());
			}

			method.setAccessible(true);
			try {
				Object[] parameterInstances = dependencyHandler.resolveParametersOf(method);
				dependencyHandler.registerInstanceOf(method.getReturnType(),
						method.invoke(configurationInstance, parameterInstances),
						dependencyAliasResolver.getAliases(method));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new AnnotationProcessingException(String.format("Cannot process Injectable: %s", method), e);
			}
		}
	}

	/**
	 * It processes classes annotated with {@link Component} and constructors,
	 * fields and methods annotated with {@link Inject}.<br>
	 * Instantiates component classes and injects its dependencies.<br>
	 * Component classes without elements annotated with {@link Inject} don't get
	 * instantiated until they're explicitly requested or referenced in other
	 * components.
	 */
	private void processComponentsAndInjections() {
		processConstructorLevelInjections();
		processFieldLevelInjections();
		processMethodLevelInjections();
	}

	/**
	 * This method is for processing constructors annotated with {@link Inject}.<br>
	 * Instantiates the component class using its annotated constructor and injects
	 * dependencies as its parameters.<br>
	 * Actually, {@link DependencyHandler} does the same in some cases, but this
	 * method forces the use of the annotated constructor.
	 */
	private void processConstructorLevelInjections() {
		List<Constructor<?>> sortedInjectConstructors = dependencySorter
				.getSortedInjectConstructors(annotationProcessorHelper.getInjectConstructors());

		for (Constructor<?> constructor : sortedInjectConstructors) {
			constructor.setAccessible(true);
			try {
				Object[] parameterInstances = dependencyHandler.resolveParametersOf(constructor);
				Object componentInstance = constructor.newInstance(parameterInstances);
				dependencyHandler.registerInstanceOf(constructor.getDeclaringClass(), componentInstance);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new AnnotationProcessingException(
						String.format("Cannot process constructor level Inject: %s", constructor), e);
			}
		}
	}

	/**
	 * This method is for processing fields annotated with {@link Inject}.<br>
	 * First, it instantiates the field's declaring component class. Then it
	 * instantiates an object of the field's type and sets it.
	 */
	private void processFieldLevelInjections() {
		List<Field> sortedInjectFields = dependencySorter
				.getSortedInjectFields(annotationProcessorHelper.getInjectFields());

		for (Field field : sortedInjectFields) {
			Object componentInstance = null;
			if (!Modifier.isStatic(field.getModifiers())) {
				componentInstance = dependencyHandler.getInstanceOf(field.getDeclaringClass());
			}

			field.setAccessible(true);
			try {
				field.set(componentInstance,
						dependencyHandler.getInstanceOf(field.getType(), dependencyAliasResolver.getAlias(field)));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new AnnotationProcessingException(String.format("Cannot process field level Inject: %s", field),
						e);
			}
		}
	}

	/**
	 * This method is for processing methods annotated with {@link Inject}.<br>
	 * First, it instantiates the field's declaring component class. Then it
	 * instantiates an object for each parameter type and invokes the annotated
	 * methods with the given parameters.
	 */
	private void processMethodLevelInjections() {
		List<Method> sortedInjectMethods = dependencySorter
				.getSortedInjectMethods(annotationProcessorHelper.getInjectMethods());

		for (Method method : sortedInjectMethods) {
			Object componentInstance = null;
			if (!Modifier.isStatic(method.getModifiers())) {
				componentInstance = dependencyHandler.getInstanceOf(method.getDeclaringClass());
			}

			method.setAccessible(true);
			try {
				Object[] parameterInstances = dependencyHandler.resolveParametersOf(method);
				method.invoke(componentInstance, parameterInstances);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new AnnotationProcessingException(String.format("Cannot process method level Inject: %s", method),
						e);
			}
		}
	}

	/**
	 * Method for processing fields annotated with {@link Value}.
	 */
	private void processValues() {
		Set<Field> valueFields = annotationProcessorHelper.getValueFields();

		for (Field field : valueFields) {
			Object componentInstance = null;
			if (!Modifier.isStatic(field.getModifiers())) {
				componentInstance = dependencyHandler.getInstanceOf(field.getDeclaringClass());
			}

			field.setAccessible(true);
			try {
				field.set(componentInstance, valueResolver.getValueOf(field));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new AnnotationProcessingException(String.format("Cannot process value field: %s", field), e);
			}
		}
	}

}
