package hu.alextoth.injector.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.google.common.collect.Sets;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.exception.DependencyAliasResolverException;

/**
 * Class for resolving dependency aliases.
 * 
 * @author Alex Toth
 */
public class DependencyAliasResolver {

	private final Set<Class<?>> aliasAnnotations;

	public DependencyAliasResolver(Reflections reflections) {
		aliasAnnotations = Sets.newHashSet(Alias.class);
		aliasAnnotations.addAll(reflections.getTypesAnnotatedWith(Alias.class).stream()
				.filter(clazz -> clazz.isAnnotation())
				.collect(Collectors.toSet()));
	}

	/**
	 * Returns the value of the {@link Alias} annotation (or its alternative) for
	 * the given field, or the default alias if it isn't annotated.
	 * 
	 * @param field A field of which dependency alias must be returned.
	 * @return The dependency alias of the given field.
	 */
	public String getAlias(Field field) {
		return getAlias((AnnotatedElement) field);
	}

	/**
	 * Returns the value of the {@link Alias} annotation (or its alternative) for
	 * the given parameter, or the default alias if it isn't annotated.
	 * 
	 * @param parameter A parameter of which dependency alias must be returned.
	 * @return The dependency alias of the given parameter.
	 */
	public String getAlias(Parameter parameter) {
		return getAlias((AnnotatedElement) parameter);
	}

	/**
	 * Returns the value of the {@link Alias} annotation (or its alternative) for
	 * the given element, or the default alias if it isn't annotated.
	 * 
	 * @param element An element of which dependency alias must be returned.
	 * @return The dependency alias of the given parameter.
	 */
	private String getAlias(AnnotatedElement element) {
		String alias = Alias.DEFAULT_ALIAS;

		for (Annotation annotation : element.getAnnotations()) {
			if (aliasAnnotations.contains(annotation.annotationType())) {
				alias = extractAliasValue(annotation);
				break;
			}
		}

		return alias;
	}

	/**
	 * Extracts the alias value from the given annotation instance.
	 * 
	 * @param annotation Annotation instance of which the alias value must be
	 *                   extracted.
	 * @return The extracted alias value of the given annotation instance.
	 * @throws DependencyAliasResolverException If the annotation instance hasn't
	 *                                          got an attribute containing alias
	 *                                          value.
	 */
	private String extractAliasValue(Annotation annotation) throws DependencyAliasResolverException {
		String alias = Alias.DEFAULT_ALIAS;

		Class<?> aliasClass = annotation.annotationType();
		Object aliasInstance = aliasClass.cast(annotation);

		Alias aliasAnnotation = aliasClass.getAnnotation(Alias.class);
		String aliasValueAttributeName = aliasAnnotation == null ? Alias.DEFAULT_ALIAS_VALUE_ATTRIBUTE_NAME
				: aliasAnnotation.aliasValueAttributeName();

		try {
			Method aliasValueAttribute = aliasClass.getMethod(aliasValueAttributeName);

			alias = String.valueOf(aliasValueAttribute.invoke(aliasInstance));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new DependencyAliasResolverException(String
					.format("Annotation %s must have '%s' attribute to be used as alias.", aliasClass.getName(),
							aliasValueAttributeName),
					e);
		}

		return alias;
	}

}
