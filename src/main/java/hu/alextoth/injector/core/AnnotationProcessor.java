package hu.alextoth.injector.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
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

	public AnnotationProcessor(AnnotationProcessorHelper annotationProcessorHelper, DependencyHandler dependencyHandler,
			DependencyAliasResolver dependencyAliasResolver) {
		this.annotationProcessorHelper = annotationProcessorHelper;
		this.dependencyHandler = dependencyHandler;
		this.dependencyAliasResolver = dependencyAliasResolver;

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
		Set<Method> injectableMethods = annotationProcessorHelper.getInjectableMethods();
		List<Method> sortedInjectableMethods = dependencySorter.getSortedInjectableMethods(injectableMethods);

		for (Method method : sortedInjectableMethods) {
			Class<?> configurationClass = method.getDeclaringClass();
			if (!annotationProcessorHelper.isConfigurationClass(configurationClass)) {
				continue;
			}

			Object configurationInstance = dependencyHandler.getInstanceOf(configurationClass);

			method.setAccessible(true);
			Object[] parameterInstances = resolveParameters(method);
			try {
				dependencyHandler.registerInstanceOf(method.getReturnType(),
						method.invoke(configurationInstance, parameterInstances),
						dependencyAliasResolver.getAliases(method));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new AnnotationProcessingException(String.format("Cannot process Injectable: %s", method));
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
		Set<Constructor<?>> constructorLevelInjections = annotationProcessorHelper.getInjectConstructors();

		for (Constructor<?> constructor : constructorLevelInjections) {
			Class<?> componentClass = constructor.getDeclaringClass();
			if (!annotationProcessorHelper.isComponentClass(componentClass)) {
				continue;
			}

			constructor.setAccessible(true);
			Object[] parameterInstances = resolveParameters(constructor);
			try {
				Object componentInstance = constructor.newInstance(parameterInstances);
				dependencyHandler.registerInstanceOf(componentClass, componentInstance);
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
		Set<Field> fieldLevelInjections = annotationProcessorHelper.getInjectFields();

		for (Field field : fieldLevelInjections) {
			Class<?> componentClass = field.getDeclaringClass();
			if (!annotationProcessorHelper.isComponentClass(componentClass)) {
				continue;
			}

			Object componentInstance = dependencyHandler.getInstanceOf(componentClass);

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
		Set<Method> methodLevelInjections = annotationProcessorHelper.getInjectMethods();

		for (Method method : methodLevelInjections) {
			Class<?> componentClass = method.getDeclaringClass();
			if (!annotationProcessorHelper.isComponentClass(componentClass)) {
				continue;
			}

			Object componentInstance = dependencyHandler.getInstanceOf(componentClass);

			method.setAccessible(true);
			Object[] parameterInstances = resolveParameters(method);
			try {
				method.invoke(componentInstance, parameterInstances);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new AnnotationProcessingException(String.format("Cannot process method level Inject: %s", method),
						e);
			}
		}
	}

	// TODO: maybe I should move this method to a ParameterResolver class
	private Object[] resolveParameters(Executable executable) {
		Parameter[] parameters = executable.getParameters();
		Object[] parameterInstances = new Object[parameters.length];

		for (int i = 0; i < parameters.length; i++) {
			parameterInstances[i] = dependencyHandler.getInstanceOf(parameters[i].getType(),
					dependencyAliasResolver.getAlias(parameters[i]));
		}

		return parameterInstances;
	}

}
