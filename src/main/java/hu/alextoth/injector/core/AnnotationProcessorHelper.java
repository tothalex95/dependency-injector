package hu.alextoth.injector.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.google.common.collect.Sets;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.annotation.Value;
import hu.alextoth.injector.util.ClassUtils;

/**
 * Helper class for processing {@link Component}, {@link Configuration},
 * {@link Injectable}, {@link Inject}, {@link Alias} and {@link Value}
 * annotations.
 * 
 * @author Alex Toth
 */
public class AnnotationProcessorHelper {

	private final Map<Class<? extends Annotation>, Set<Class<? extends Annotation>>> annotations;

	private final Reflections reflections;

	public AnnotationProcessorHelper(Reflections reflections) {
		annotations = new HashMap<>(5);

		this.reflections = reflections;
	}

	/**
	 * Returns a set of annotation types annotated with {@link Component}.
	 * 
	 * @return A set of annotation types annotated with {@link Component}.
	 */
	public Set<Class<? extends Annotation>> getComponentAnnotations() {
		return getAnnotationsAnnotatedWith(Component.class);
	}

	/**
	 * Returns a set of annotation types annotated with {@link Configuration}.
	 * 
	 * @return A set of annotation types annotated with {@link Configuration}.
	 */
	public Set<Class<? extends Annotation>> getConfigurationAnnotations() {
		return getAnnotationsAnnotatedWith(Configuration.class);
	}

	/**
	 * Returns a set of annotation types annotated with {@link Injectable}.
	 * 
	 * @return A set of annotation types annotated with {@link Injectable}.
	 */
	public Set<Class<? extends Annotation>> getInjectableAnnotations() {
		return getAnnotationsAnnotatedWith(Injectable.class);
	}

	/**
	 * Returns a set of annotation types annotated with {@link Inject}.
	 * 
	 * @return A set of annotation types annotated with {@link Inject}.
	 */
	public Set<Class<? extends Annotation>> getInjectAnnotations() {
		return getAnnotationsAnnotatedWith(Inject.class);
	}

	/**
	 * Returns a set of annotation types annotated with {@link Alias}.
	 * 
	 * @return A set of annotation types annotated with {@link Alias}.
	 */
	public Set<Class<? extends Annotation>> getAliasAnnotations() {
		return getAnnotationsAnnotatedWith(Alias.class);
	}

	/**
	 * Returns a set of annotation types annotated with {@link Value}.
	 * 
	 * @return A set of annotation types annotated with {@link Value}.
	 */
	public Set<Class<? extends Annotation>> getValueAnnotations() {
		return getAnnotationsAnnotatedWith(Value.class);
	}

	/**
	 * Returns a boolean value indicating whether the given annotation is component
	 * annotation or not.
	 * 
	 * @param annotation Annotation to check whether it is a component annotation or
	 *                   not.
	 * @return A boolean value indicating whether the given annotation is component
	 *         annotation or not.
	 */
	public boolean isComponentAnnotation(Class<? extends Annotation> annotation) {
		return getComponentAnnotations().contains(annotation);
	}

	/**
	 * Returns a boolean value indicating whether the given annotation is
	 * configuration annotation or not.
	 * 
	 * @param annotation Annotation to check whether it is a configuration
	 *                   annotation or not.
	 * @return A boolean value indicating whether the given annotation is
	 *         configuration annotation or not.
	 */
	public boolean isConfigurationAnnotation(Class<? extends Annotation> annotation) {
		return getConfigurationAnnotations().contains(annotation);
	}

	/**
	 * Returns a boolean value indicating whether the given annotation is injectable
	 * annotation or not.
	 * 
	 * @param annotation Annotation to check whether it is a injectable annotation
	 *                   or not.
	 * @return A boolean value indicating whether the given annotation is injectable
	 *         annotation or not.
	 */
	public boolean isInjectableAnnotation(Class<? extends Annotation> annotation) {
		return getInjectableAnnotations().contains(annotation);
	}

	/**
	 * Returns a boolean value indicating whether the given annotation is inject
	 * annotation or not.
	 * 
	 * @param annotation Annotation to check whether it is a inject annotation or
	 *                   not.
	 * @return A boolean value indicating whether the given annotation is inject
	 *         annotation or not.
	 */
	public boolean isInjectAnnotation(Class<? extends Annotation> annotation) {
		return getInjectAnnotations().contains(annotation);
	}

	/**
	 * Returns a boolean value indicating whether the given annotation is alias
	 * annotation or not.
	 * 
	 * @param annotation Annotation to check whether it is alias annotation or not.
	 * @return A boolean value indicating whether the given annotation is alias
	 *         annotation or not.
	 */
	public boolean isAliasAnnotation(Class<? extends Annotation> annotation) {
		return getAliasAnnotations().contains(annotation);
	}

	/**
	 * Returns a boolean value indicating whether the given annotation is value
	 * annotation or not.
	 * 
	 * @param annotation Annotation to check whether it is value annotation or not.
	 * @return A boolean value indicating whether the given annotation is value
	 *         annotation or not.
	 */
	public boolean isValueAnnotation(Class<? extends Annotation> annotation) {
		return getValueAnnotations().contains(annotation);
	}

	/**
	 * Returns a boolean value indicating whether the given class is a component
	 * class or not.
	 * 
	 * @param clazz Class to check whether it is a component class or not.
	 * @return A boolean value indicating whether the given class is a component
	 *         class or not.
	 */
	public boolean isComponentClass(Class<?> clazz) {
		return Arrays.stream(clazz.getAnnotations())
				.anyMatch(annotation -> isComponentAnnotation(annotation.annotationType()));
	}

	/**
	 * Returns a boolean value indicating whether the given class is a configuration
	 * class or not.
	 * 
	 * @param clazz Class to check whether it is a configuration class or not.
	 * @return A boolean value indicating whether the given class is a configuration
	 *         class or not.
	 */
	public boolean isConfigurationClass(Class<?> clazz) {
		return Arrays.stream(clazz.getAnnotations())
				.anyMatch(annotation -> isConfigurationAnnotation(annotation.annotationType()));
	}

	/**
	 * Returns a boolean value indicating whether the given method is an injactable
	 * method or not.
	 * 
	 * @param method Method to check whether it is an injectable method or not.
	 * @return A boolean value indicating whether the given method is an injectable
	 *         method or not.
	 */
	public boolean isInjectableMethod(Method method) {
		if (!canBeUsedAsInjectableMethod(method)) {
			return false;
		}
		return Arrays.stream(method.getAnnotations())
				.anyMatch(annotation -> isInjectableAnnotation(annotation.annotationType()));
	}

	/**
	 * Returns a boolean value indicating whether the given constructor must be used
	 * for injection or not.
	 * 
	 * @param constructor Constructor to check whether it must be used for injection
	 *                    or not.
	 * @return A boolean value indicating whether the given constructor must be used
	 *         for injection or not.
	 */
	public boolean isInjectConstructor(Constructor<?> constructor) {
		if (!canBeUsedAsInjectConstructor(constructor)) {
			return false;
		}
		return Arrays.stream(constructor.getAnnotations())
				.anyMatch(annotation -> isInjectAnnotation(annotation.annotationType()));
	}

	/**
	 * Returns a boolean value indicating whether the given field must be used for
	 * injection or not.
	 * 
	 * @param field Field to check whether it must be used for injection or not.
	 * @return A boolean value indicating whether the given field must be used for
	 *         injection or not.
	 */
	public boolean isInjectField(Field field) {
		if (!canBeUsedAsInjectField(field)) {
			return false;
		}
		return Arrays.stream(field.getAnnotations())
				.anyMatch(annotation -> isInjectAnnotation(annotation.annotationType()));
	}

	/**
	 * Returns a boolean value indicating whether the given method must be used for
	 * injection or not.
	 * 
	 * @param method Method to check whether it must be used for injection or not.
	 * @return A boolean value indicating whether the given method must be used for
	 *         injection or not.
	 */
	public boolean isInjectMethod(Method method) {
		if (!canBeUsedAsInjectMethod(method)) {
			return false;
		}
		return Arrays.stream(method.getAnnotations())
				.anyMatch(annotation -> isInjectAnnotation(annotation.annotationType()));
	}

	/**
	 * Returns a boolean value indicating whether the given field's value must be
	 * set or not.
	 * 
	 * @param field Field to check whether its value must be set or not.
	 * @return A boolean value indicating whether the given field's value must be
	 *         set or not.
	 */
	public boolean isValueField(Field field) {
		if (!canBeUsedAsValueField(field)) {
			return false;
		}
		return Arrays.stream(field.getAnnotations())
				.anyMatch(annotation -> isValueAnnotation(annotation.annotationType()));
	}

	/**
	 * Returns a set of component classes.
	 * 
	 * @return A set of component classes.
	 */
	public Set<Class<?>> getComponentClasses() {
		Set<Class<?>> componentClasses = Sets.newHashSet();

		getComponentAnnotations().forEach(annotation -> componentClasses.addAll(getClassesAnnotatedWith(annotation)));

		return componentClasses;
	}

	/**
	 * Returns a set of configuration classes.
	 * 
	 * @return A set of configuration classes.
	 */
	public Set<Class<?>> getConfigurationClasses() {
		Set<Class<?>> configurationClasses = Sets.newHashSet();

		getConfigurationAnnotations()
				.forEach(annotation -> configurationClasses.addAll(getClassesAnnotatedWith(annotation)));

		return configurationClasses;
	}

	/**
	 * Returns a set of injectable methods.
	 * 
	 * @return A set of injectable methods.
	 */
	public Set<Method> getInjectableMethods() {
		Set<Method> injectableMethods = Sets.newHashSet();

		getInjectableAnnotations().forEach(annotation -> injectableMethods.addAll(getMethodsAnnotatedWith(annotation)));

		return injectableMethods.stream()
				.filter(this::canBeUsedAsInjectableMethod)
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a set of constructors annotated with {@link Inject} or its
	 * alternatives.
	 * 
	 * @return A set of constructors annotated with {@link Inject} or its
	 *         alternatives.
	 */
	public Set<Constructor<?>> getInjectConstructors() {
		Set<Constructor<?>> injectConstructors = Sets.newHashSet();

		getInjectAnnotations()
				.forEach(annotation -> injectConstructors.addAll(getConstructorsAnnotatedWith(annotation)));

		return injectConstructors.stream()
				.filter(this::canBeUsedAsInjectConstructor)
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a set of fields annotated with {@link Inject} or its alternatives.
	 * 
	 * @return A set of fields annotated with {@link Inject} or its alternatives.
	 */
	public Set<Field> getInjectFields() {
		Set<Field> injectFields = Sets.newHashSet();

		getInjectAnnotations().forEach(annotation -> injectFields.addAll(getFieldsAnnotatedWith(annotation)));

		return injectFields.stream()
				.filter(this::canBeUsedAsInjectField)
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a set of methods annotated with {@link Inject} or its alternatives.
	 * 
	 * @return A set of methods annotated with {@link Inject} or its alternatives.
	 */
	public Set<Method> getInjectMethods() {
		Set<Method> injectMethods = Sets.newHashSet();

		getInjectAnnotations().forEach(annotation -> injectMethods.addAll(getMethodsAnnotatedWith(annotation)));

		return injectMethods.stream()
				.filter(this::canBeUsedAsInjectMethod)
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a set of methods annotated with {@link Value} or its alternatives.
	 * 
	 * @return A set of methods annotated with {@link Value} or its alternatives.
	 */
	public Set<Field> getValueFields() {
		Set<Field> valueFields = Sets.newHashSet();

		getValueAnnotations().forEach(annotation -> valueFields.addAll(getFieldsAnnotatedWith(annotation)));

		return valueFields.stream()
				.filter(this::canBeUsedAsValueField)
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a set of annotation types annotated with the given annotation.
	 * 
	 * @param annotation
	 * @return A set of annotation types annotated with the given annotation.
	 */
	@SuppressWarnings("unchecked")
	private Set<Class<? extends Annotation>> getAnnotationsAnnotatedWith(Class<? extends Annotation> annotation) {
		Set<Class<? extends Annotation>> annotatedAnnotations = annotations.get(annotation);

		if (annotatedAnnotations != null) {
			return annotatedAnnotations;
		}

		annotatedAnnotations = Sets.newHashSet(annotation);

		annotatedAnnotations
				.addAll(reflections.getTypesAnnotatedWith(annotation).stream()
						.filter(Class::isAnnotation)
						.map(clazz -> (Class<? extends Annotation>) clazz)
						.collect(Collectors.toSet()));

		annotations.put(annotation, annotatedAnnotations);

		return annotatedAnnotations;
	}

	/**
	 * Returns a set of classes annotated with the given annotation.
	 * 
	 * @param annotation Classes annotated with this must be returned.
	 * @return A set of classes annotated with the given annotation.
	 */
	private Set<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getTypesAnnotatedWith(annotation).stream()
				.filter(clazz -> !clazz.isAnnotation())
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a set of methods annotated with the given annotation.
	 * 
	 * @param annotation Methods annotated with this must be returned.
	 * @return A set of methods annotated with the given annotation.
	 */
	private Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getMethodsAnnotatedWith(annotation);
	}

	/**
	 * Returns a set of constructors annotated with the given annotation.
	 * 
	 * @param annotation Constructors annotated with this must be returned.
	 * @return A set of constructors annotated with the given annotation.
	 */
	private Set<Constructor<?>> getConstructorsAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getConstructorsAnnotatedWith(annotation).stream()
				.map(constructor -> (Constructor<?>) constructor)
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a set of fields annotated with the given annotation.
	 * 
	 * @param annotation Fields annotated with this must be returned.
	 * @return A set of fields annotated with the given annotation.
	 */
	private Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getFieldsAnnotatedWith(annotation);
	}

	/**
	 * Returns a boolean value indicating whether the given method can be used as
	 * injectable or not.
	 * 
	 * @param method Method to check whether it can be used as injectable or not.
	 * @return A boolean value indicating whether the given method can be used as
	 *         injectable or not.
	 */
	private boolean canBeUsedAsInjectableMethod(Method method) {
		return !Void.TYPE.equals(method.getReturnType()) && isConfigurationClass(method.getDeclaringClass());
	}

	/**
	 * Returns a boolean value indicating whether the given constructor can be used
	 * for injection or not.
	 * 
	 * @param constructor Constructor to check whether it can be used for injection
	 *                    or not.
	 * @return A boolean value indicating whether the given constructor can be used
	 *         for injection or not.
	 */
	private boolean canBeUsedAsInjectConstructor(Constructor<?> constructor) {
		return isComponentClass(constructor.getDeclaringClass());
	}

	/**
	 * Returns a boolean value indicating whether the given field can be used for
	 * injection or not.
	 * 
	 * @param field Field to check whether it can be used for injection or not.
	 * @return A boolean value indicating whether the given field can be used for
	 *         injection or not.
	 */
	private boolean canBeUsedAsInjectField(Field field) {
		return isComponentClass(field.getDeclaringClass());
	}

	/**
	 * Returns a boolean value indicating whether the given method can be used for
	 * injection or not.
	 * 
	 * @param method Method to check whether it can be used for injection or not.
	 * @return A boolean value indicating whether the given method can be used for
	 *         injection or not.
	 */
	private boolean canBeUsedAsInjectMethod(Method method) {
		return isComponentClass(method.getDeclaringClass());
	}

	/**
	 * Returns a boolean value indicating whether the given field's value can be set
	 * or not.
	 * 
	 * @param field Field to check whether its value can be set or not.
	 * @return A boolean value indicating whether the given field's value can be set
	 *         or not.
	 */
	private boolean canBeUsedAsValueField(Field field) {
		return isComponentClass(field.getDeclaringClass())
				&& (ClassUtils.isPrimitiveOrWrapper(field.getType()) || String.class.isAssignableFrom(field.getType()));
	}

}
