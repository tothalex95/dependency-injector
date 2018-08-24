package hu.alextoth.injector.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Sets;

import hu.alextoth.injector.annotation.Alias;

/**
 * Class for resolving dependency aliases.
 * 
 * @author Alex Toth
 */
public class DependencyAliasResolver {

	private final Set<Class<?>> aliasAnnotations;

	public DependencyAliasResolver(Reflections reflections) {
		aliasAnnotations = Sets.newHashSet(Alias.class);
//		aliasAnnotations.addAll(reflections.getTypesAnnotatedWith(Alias.class).stream()
//				.filter(clazz -> clazz.isAnnotation())
//				.collect(Collectors.toSet()));
	}

	/**
	 * Returns the value of the {@link Alias} annotation (or its alternative) for
	 * the given field, or the default alias if it isn't annotated.
	 * 
	 * @param field A field of which dependency alias must be returned.
	 * @return The dependency alias of the given field.
	 */
	public String getAlias(Field field) {
		String alias = Alias.DEFAULT_ALIAS;

		for (Annotation annotation : field.getAnnotations()) {
			if (aliasAnnotations.contains(annotation.annotationType())) {
				// TODO: fix this to support custom annotations (error on other than @Alias)
				alias = ((Alias) annotation).value();
				break;
			}
		}

		return alias;
	}

	/**
	 * Returns the value of the {@link Alias} annotation (or its alternative) for
	 * the given parameter, or the default alias if it isn't annotated.
	 * 
	 * @param parameter A parameter of which dependency alias must be returned.
	 * @return The dependency alias of the given parameter.
	 */
	public String getAlias(Parameter parameter) {
		String alias = Alias.DEFAULT_ALIAS;

		for (Annotation annotation : parameter.getAnnotations()) {
			if (aliasAnnotations.contains(annotation.annotationType())) {
				// TODO: fix this to support custom annotations (error on other than @Alias)
				alias = ((Alias) annotation).value();
				break;
			}
		}

		return alias;
	}

}
