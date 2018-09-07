package hu.alextoth.injector.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.exception.DependencyCreationException;

/**
 * Class for managing instances of dependency classes.
 * 
 * @author Alex Toth
 */
public class DependencyHandler {

	private final Map<Class<?>, Map<String, Object>> dependencies;

	private final Reflections reflections;
	private final DependencyAliasResolver dependencyAliasResolver;

	public DependencyHandler(Reflections reflections, DependencyAliasResolver dependencyAliasResolver) {
		dependencies = new HashMap<>();

		this.reflections = reflections;
		this.dependencyAliasResolver = dependencyAliasResolver;
	}

	/**
	 * Returns, or creates if necessary, an instance of the given class.
	 * 
	 * @param clazz Class of which an instance must be returned.
	 * @param alias Alias of the requested instance.
	 * @return A registered instance of the given class.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstanceOf(Class<T> clazz, String alias) {
		Map<String, Object> namedDependencies = dependencies.get(clazz);

		T instance = null;

		if (namedDependencies == null || (instance = (T) namedDependencies.get(alias)) == null) {
			instance = createInstanceOf(clazz, alias);
		}

		return instance;
	}

	/**
	 * @see hu.alextoth.injector.core.DependencyHandler#getInstanceOf(Class, String)
	 */
	public <T> T getInstanceOf(Class<T> clazz) {
		return getInstanceOf(clazz, Alias.DEFAULT_ALIAS);
	}

	/**
	 * Creates and registers an instance of the given class.<br>
	 * Returns the created instance.
	 * 
	 * @param clazz Class to be instantiated.
	 * @param alias Alias for the requested instance.
	 * @return An instance of the given class.
	 */
	public <T> T createInstanceOf(Class<T> clazz, String alias) {
		T instance = null;

		Constructor<? extends T> constructor = getSuitableConstructor(clazz);

		Parameter[] parameters = constructor.getParameters();
		Object[] parameterInstances = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterInstances[i] = getInstanceOf(parameters[i].getType(),
					dependencyAliasResolver.getAlias(parameters[i]));
		}

		try {
			instance = constructor.newInstance(parameterInstances);
			registerInstanceOf(clazz, instance, alias);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new DependencyCreationException(String.format("Cannot instantiate %s", clazz), e);
		}

		return instance;
	}

	/**
	 * @see hu.alextoth.injector.core.DependencyHandler#createInstanceOf(Class,
	 *      String)
	 */
	public <T> T createInstanceOf(Class<T> clazz) {
		return createInstanceOf(clazz, Alias.DEFAULT_ALIAS);
	}

	/**
	 * Registers an instance for the given class with the given aliases.
	 * 
	 * @param clazz    Class for which the instance must be registered.
	 * @param instance Class instance to be registered.
	 * @param aliases  Aliases for the instance to be registered.
	 */
	public void registerInstanceOf(Class<?> clazz, Object instance, String... aliases) {
		Map<String, Object> namedDependencies = dependencies.get(clazz);

		if (namedDependencies == null) {
			namedDependencies = new HashMap<>();
		}

		if (aliases == null || aliases.length == 0) {
			namedDependencies.put(Alias.DEFAULT_ALIAS, instance);
		} else {
			for (String alias : aliases) {
				namedDependencies.put(alias, instance);
			}
		}

		dependencies.put(clazz, namedDependencies);
	}
	
	/**
	 * Finds a suitable, instantiable class for the given class and returns its
	 * constructor.<br>
	 * If the given class itself is instantiable, returns its constructor. In other
	 * cases (interface or abstract class) looks for a suitable class and returns an
	 * appropriate constructor.
	 * 
	 * @param clazz Class for which an appropriate constructor needs to be returned.
	 * @return An appropriate constructor for the given class.
	 */
	@SuppressWarnings("unchecked")
	private <T> Constructor<? extends T> getSuitableConstructor(Class<T> clazz) {
		Constructor<? extends T> constructor = null;

		if (Modifier.isInterface(clazz.getModifiers()) || Modifier.isAbstract(clazz.getModifiers())) {
			List<Class<? extends T>> suitableClasses = reflections.getSubTypesOf(clazz).stream()
					.filter(c -> !Modifier.isInterface(c.getModifiers()) && !Modifier.isAbstract(c.getModifiers()))
					.collect(Collectors.toList());

			int numberOfSuitableClasses = suitableClasses.size();
			if (numberOfSuitableClasses == 0) {
				throw new DependencyCreationException(
						String.format("Cannot find suitable implementation for %s", clazz));
			}
			if (numberOfSuitableClasses > 1) {
				throw new DependencyCreationException(
						String.format("Found too many suitable implementations for %s", clazz));
			}

			constructor = getSuitableConstructor(suitableClasses.get(0));
		} else {
			Constructor<T>[] declaredConstuctors = (Constructor<T>[]) clazz.getDeclaredConstructors();
			constructor = declaredConstuctors[0];
		}

		return constructor;
	}

}
