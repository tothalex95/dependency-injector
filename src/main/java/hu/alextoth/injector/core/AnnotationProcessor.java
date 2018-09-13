package hu.alextoth.injector.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.google.common.collect.Sets;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.exception.AnnotationProcessingException;

/**
 * Class for processing {@link Configuration}, {@link Injectable},
 * {@link Component} and {@link Inject} annotations and their alternatives.
 * 
 * @author Alex Toth
 */
public class AnnotationProcessor {

	private final Map<Class<? extends Annotation>, Set<Class<?>>> annotations;

	private final Reflections reflections;
	private final DependencyHandler dependencyHandler;
	private final DependencyAliasResolver dependencyAliasResolver;

	public AnnotationProcessor(Reflections reflections, DependencyHandler dependencyHandler,
			DependencyAliasResolver dependencyAliasResolver) {
		this.annotations = new HashMap<>(4);

		this.reflections = reflections;
		this.dependencyHandler = dependencyHandler;
		this.dependencyAliasResolver = dependencyAliasResolver;
	}

	/**
	 * Method for starting the processing of {@link Configuration},
	 * {@link Injectable}, {@link Component} and {@link Inject} annotations and
	 * their alternatives.
	 */
	public void processAnnotations() {
		registerAnnotationsToCheck();
		processConfigurationsAndInjectables();
		processComponentsAndInjections();
	}

	/**
	 * Registers annotations that need to be checked by the annotation processor.
	 * Basically, it looks up annotations annotated with {@link Configuration},
	 * {@link Injectable}, {@link Component} or {@link Inject}.
	 */
	@SuppressWarnings("unchecked")
	private void registerAnnotationsToCheck() {
		annotations.clear();

		Set<Class<? extends Annotation>> basicAnnotations = Sets.newHashSet(Configuration.class, Injectable.class,
				Component.class, Inject.class);

		for (Class<? extends Annotation> annotation : basicAnnotations) {
			Set<Class<?>> similarAnnotations = Sets.newHashSet(annotation);
			similarAnnotations.addAll(reflections.getTypesAnnotatedWith(annotation).stream()
					.filter(clazz -> clazz.isAnnotation()).collect(Collectors.toSet()));
			annotations.put(annotation, similarAnnotations);
		}
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
		Set<Method> injectables = getInjectables();
		Set<Class<?>> configurations = getConfigurations();

		for (Method method : injectables) {
			Class<?> configurationClass = method.getDeclaringClass();
			if (!configurations.contains(configurationClass)) {
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
	@SuppressWarnings("rawtypes")
	private void processConstructorLevelInjections() {
		Set<Constructor> constructorLevelInjections = getConstructorLevelInjections();
		Set<Class<?>> components = getComponents();

		for (Constructor<?> constructor : constructorLevelInjections) {
			Class<?> componentClass = constructor.getDeclaringClass();
			if (!components.contains(componentClass)) {
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
		Set<Field> fieldLevelInjections = getFieldLevelInjections();
		Set<Class<?>> components = getComponents();

		for (Field field : fieldLevelInjections) {
			Class<?> componentClass = field.getDeclaringClass();
			if (!components.contains(componentClass)) {
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
		Set<Method> methodLevelInjections = getMethodLevelInjections();
		Set<Class<?>> components = getComponents();

		for (Method method : methodLevelInjections) {
			Class<?> componentClass = method.getDeclaringClass();
			if (!components.contains(componentClass)) {
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

	/**
	 * Returns a set of methods annotated with {@link Injectable} or its
	 * alternatives.
	 * 
	 * @return A set of methods annotated with {@link Injectable} or its
	 *         alternatives.
	 */
	@SuppressWarnings("unchecked")
	private Set<Method> getInjectables() {
		Set<Method> injectables = Sets.newHashSet();

		annotations.get(Injectable.class)
				.forEach(annotation -> injectables.addAll(reflections
						.getMethodsAnnotatedWith((Class<? extends Annotation>) annotation).stream()
						.filter(method -> !method.getReturnType().equals(Void.TYPE)).collect(Collectors.toSet())));

		return injectables;
	}

	/**
	 * Returns a set of classes annotated with {@link Configuration} or its
	 * alternatives.
	 * 
	 * @return A set of classes annotated with {@link Configuration} or its
	 *         alternatives.
	 */
	@SuppressWarnings("unchecked")
	private Set<Class<?>> getConfigurations() {
		Set<Class<?>> configurations = Sets.newHashSet();

		annotations.get(Configuration.class)
				.forEach(annotation -> configurations
						.addAll(reflections.getTypesAnnotatedWith((Class<? extends Annotation>) annotation).stream()
								.filter(clazz -> !clazz.isAnnotation()).collect(Collectors.toSet())));

		return configurations;
	}

	/**
	 * Returns a set of constructors annotated with {@link Inject} or its
	 * alternatives.
	 * 
	 * @return A set of constructors annotated with {@link Inject} or its
	 *         alternatives.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set<Constructor> getConstructorLevelInjections() {
		Set<Constructor> constructorLevelInjections = Sets.newHashSet();

		annotations.get(Inject.class).forEach(annotation -> constructorLevelInjections
				.addAll(reflections.getConstructorsAnnotatedWith((Class<? extends Annotation>) annotation)));

		return constructorLevelInjections;
	}

	/**
	 * Returns a set of fields annotated with {@link Inject} or its alternatives.
	 * 
	 * @return A set of fields annotated with {@link Inject} or its alternatives.
	 */
	@SuppressWarnings("unchecked")
	private Set<Field> getFieldLevelInjections() {
		Set<Field> fieldLevelInjections = Sets.newHashSet();

		annotations.get(Inject.class).forEach(annotation -> fieldLevelInjections
				.addAll(reflections.getFieldsAnnotatedWith((Class<? extends Annotation>) annotation)));

		return fieldLevelInjections;
	}

	/**
	 * Returns a set of methods annotated with {@link Inject} or its alternatives.
	 * 
	 * @return A set of methods annotated with {@link Inject} or its alternatives.
	 */
	@SuppressWarnings("unchecked")
	private Set<Method> getMethodLevelInjections() {
		Set<Method> methodLevelInjections = Sets.newHashSet();

		annotations.get(Inject.class).forEach(annotation -> methodLevelInjections
				.addAll(reflections.getMethodsAnnotatedWith((Class<? extends Annotation>) annotation)));

		return methodLevelInjections;
	}

	/**
	 * Returns a set of classes annotated with {@link Component} or its
	 * alternatives.
	 * 
	 * @return A set of classes annotated with {@link Component} or its
	 *         alternatives.
	 */
	@SuppressWarnings("unchecked")
	private Set<Class<?>> getComponents() {
		Set<Class<?>> components = Sets.newHashSet();

		annotations.get(Component.class)
				.forEach(annotation -> components
						.addAll(reflections.getTypesAnnotatedWith((Class<? extends Annotation>) annotation).stream()
								.filter(clazz -> !clazz.isAnnotation()).collect(Collectors.toSet())));

		return components;
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
