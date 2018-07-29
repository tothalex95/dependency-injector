package hu.alextoth.injector.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.exception.AnnotationProcessingException;

/**
 * Class for processing {@link Configuration}, {@link Injectable},
 * {@link Component} and {@link Inject} annotations.
 * 
 * @author Alex Toth
 */
public class AnnotationProcessor {

	private final Reflections reflections;
	private final DependencyHandler dependencyHandler;

	public AnnotationProcessor(String basePackage) {
		reflections = new Reflections(
				new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(),
								new FieldAnnotationsScanner(), new MethodAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage(basePackage)));
		dependencyHandler = new DependencyHandler();
	}

	public AnnotationProcessor() {
		this("");
	}

	/**
	 * Method for starting the processing of {@link Configuration},
	 * {@link Injectable}, {@link Component} and {@link Inject} annotations.
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
		for (Method method : reflections.getMethodsAnnotatedWith(Injectable.class)) {
			Class<?> configurationClass = method.getDeclaringClass();
			if (!configurationClass.isAnnotationPresent(Configuration.class)) {
				continue;
			}

			Object configurationInstance = dependencyHandler.getInstanceOf(configurationClass);

			method.setAccessible(true);
			try {
				dependencyHandler.registerInstanceOf(method.getReturnType(), method.invoke(configurationInstance));
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
		for (Constructor<?> constructor : reflections.getConstructorsAnnotatedWith(Inject.class)) {
			Class<?> componentClass = constructor.getDeclaringClass();
			if (!componentClass.isAnnotationPresent(Component.class)) {
				continue;
			}

			constructor.setAccessible(true);
			Class<?>[] parameterClasses = constructor.getParameterTypes();
			Object[] parameterInstances = new Object[parameterClasses.length];
			for (int i = 0; i < parameterClasses.length; i++) {
				parameterInstances[i] = dependencyHandler.getInstanceOf(parameterClasses[i]);
			}
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
		for (Field field : reflections.getFieldsAnnotatedWith(Inject.class)) {
			Class<?> componentClass = field.getDeclaringClass();
			if (!componentClass.isAnnotationPresent(Component.class)) {
				continue;
			}

			Object componentInstance = dependencyHandler.getInstanceOf(componentClass);

			field.setAccessible(true);
			try {
				field.set(componentInstance, dependencyHandler.getInstanceOf(field.getType()));
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
		for (Method method : reflections.getMethodsAnnotatedWith(Inject.class)) {
			Class<?> componentClass = method.getDeclaringClass();
			if (!componentClass.isAnnotationPresent(Component.class)) {
				continue;
			}

			Object componentInstance = dependencyHandler.getInstanceOf(componentClass);

			method.setAccessible(true);
			Class<?>[] parameterClasses = method.getParameterTypes();
			Object[] parameterInstances = new Object[parameterClasses.length];
			for (int i = 0; i < parameterClasses.length; i++) {
				parameterInstances[i] = dependencyHandler.getInstanceOf(parameterClasses[i]);
			}
			try {
				method.invoke(componentInstance, parameterInstances);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new AnnotationProcessingException(String.format("Cannot process method level Inject: %s", method),
						e);
			}
		}
	}

}
