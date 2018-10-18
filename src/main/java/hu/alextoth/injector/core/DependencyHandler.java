package hu.alextoth.injector.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.util.ClassUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * Class for managing instances of dependency classes.
 * 
 * @author Alex Toth
 */
public class DependencyHandler {

	private final Map<Class<?>, Map<String, Object>> dependencies;

	private final Reflections reflections;
	private final AnnotationProcessorHelper annotationProcessorHelper;
	private final DependencyAliasResolver dependencyAliasResolver;

	public DependencyHandler(Reflections reflections, AnnotationProcessorHelper annotationProcessorHelper,
			DependencyAliasResolver dependencyAliasResolver) {
		dependencies = new HashMap<>();

		this.reflections = reflections;
		this.annotationProcessorHelper = annotationProcessorHelper;
		this.dependencyAliasResolver = dependencyAliasResolver;
	}

	/**
	 * Returns a boolean value indicating whether an instance of the given class
	 * with the given alias exists or not.
	 * 
	 * @param clazz
	 * @param alias
	 * @return Whether an instance of the given class with the given alias exists or
	 *         not.
	 */
	public boolean hasInstanceOf(Class<?> clazz, String alias) {
		Map<String, Object> namedDependencies = dependencies.get(clazz);

		return namedDependencies != null && namedDependencies.get(alias) != null;
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
		if (hasInstanceOf(clazz, alias)) {
			return (T) dependencies.get(clazz).get(alias);
		}
		return createInstanceOf(clazz, alias);
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
	@SuppressWarnings("unchecked")
	public <T> T createInstanceOf(Class<T> clazz, String alias) {
		T instance = null;

		Constructor<? extends T> constructor = getSuitableConstructor(clazz);

		Parameter[] parameters = constructor.getParameters();
		Object[] parameterInstances = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterInstances[i] = getInstanceOf(parameters[i].getType(),
					dependencyAliasResolver.getAlias(parameters[i]));
		}

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
			if (annotationProcessorHelper.isInjectableMethod(method)) {
				String[] aliases = dependencyAliasResolver.getAliases(method);

				if (hasInstanceOf(method.getReturnType(), aliases[0])) {
					return getInstanceOf(method.getReturnType(), aliases[0]);
				}
			}
			return proxy.invokeSuper(obj, args);
		});

		instance = (T) enhancer.create(constructor.getParameterTypes(), parameterInstances);
		registerInstanceOf(clazz, instance, alias);

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
		if (ClassUtils.isConcrete(clazz)) {
			return (Constructor<? extends T>) clazz.getDeclaredConstructors()[0];
		}

		List<Class<? extends T>> suitableClasses = reflections.getSubTypesOf(clazz).stream()
				.filter(ClassUtils::isConcrete)
				.collect(Collectors.toList());

		int numberOfSuitableClasses = suitableClasses.size();
		if (numberOfSuitableClasses == 0) {
			throw new IllegalArgumentException(String.format("Cannot find suitable implementation for %s", clazz));
		}
		if (numberOfSuitableClasses > 1) {
			throw new IllegalArgumentException(String.format("Found too many suitable implementations for %s", clazz));
		}

		return getSuitableConstructor(suitableClasses.get(0));
	}

}
