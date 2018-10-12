package hu.alextoth.injector.core;

import java.lang.annotation.Annotation;
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

/**
 * Helper class for processing {@link Component}, {@link Configuration},
 * {@link Injectable}, {@link Inject} and {@link Alias} annotations.
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
	 * @param annotation Annotation to check whether it is a alias annotation or
	 *                   not.
	 * @return A boolean value indicating whether the given annotation is alias
	 *         annotation or not.
	 */
	public boolean isAliasAnnotation(Class<? extends Annotation> annotation) {
		return getAliasAnnotations().contains(annotation);
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
		if (Void.TYPE.equals(method.getReturnType()) || !isConfigurationClass(method.getDeclaringClass())) {
			return false;
		}
		return Arrays.stream(method.getAnnotations())
				.anyMatch(annotation -> isInjectableAnnotation(annotation.annotationType()));
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
						.filter(clazz -> clazz.isAnnotation())
						.map(clazz -> (Class<? extends Annotation>) clazz)
						.collect(Collectors.toSet()));

		annotations.put(annotation, annotatedAnnotations);

		return annotatedAnnotations;
	}

}
