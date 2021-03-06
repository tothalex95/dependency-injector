package hu.alextoth.injector.core;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.core.helper.AnnotationProcessorHelper;
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
	private final ValueResolver valueResolver;

	public DependencyHandler(Reflections reflections, AnnotationProcessorHelper annotationProcessorHelper,
			DependencyAliasResolver dependencyAliasResolver, ValueResolver valueResolver) {
		dependencies = new HashMap<>();

		this.reflections = reflections;
		this.annotationProcessorHelper = annotationProcessorHelper;
		this.dependencyAliasResolver = dependencyAliasResolver;
		this.valueResolver = valueResolver;
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
		T instance = ClassUtils.getDefaultValueForPrimitive(clazz);

		if (instance == null) {
			if (clazz.isArray()) {
				instance = (T) createArrayOf(clazz.getComponentType());
			} else if (Modifier.isFinal(clazz.getModifiers())) {
				instance = createNewInstanceOf(clazz);
			} else {
				instance = createProxyOf(clazz);
			}
		}

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
	 * Returns instances of the given executable's parameters.
	 * 
	 * @param executable Executable of which parameters must be resolved.
	 * @return Instances of the given executable's parameters.
	 */
	public Object[] resolveParametersOf(Executable executable) {
		Parameter[] parameters = executable.getParameters();
		Object[] parameterInstances = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterInstances[i] = annotationProcessorHelper.isValueParameter(parameters[i])
					? valueResolver.getValueOf(parameters[i])
					: getInstanceOf(parameters[i].getType(), dependencyAliasResolver.getAlias(parameters[i]));
		}
		return parameterInstances;
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
			Constructor<? extends T>[] constructors = (Constructor<? extends T>[]) clazz.getDeclaredConstructors();
			Arrays.sort(constructors, (constructor1, constructor2) -> {
				if (constructor1.getParameterCount() > constructor2.getParameterCount()) {
					return 1;
				}
				if (constructor1.getParameterCount() < constructor2.getParameterCount()) {
					return -1;
				}
				return 0;
			});
			return constructors[0];
		}

		List<Class<? extends T>> suitableClasses = reflections.getSubTypesOf(clazz).stream()
				.filter(ClassUtils::isConcrete).collect(Collectors.toList());

		int numberOfSuitableClasses = suitableClasses.size();
		if (numberOfSuitableClasses == 0) {
			throw new IllegalArgumentException(String.format("Cannot find suitable implementation for %s", clazz));
		}
		if (numberOfSuitableClasses > 1) {
			throw new IllegalArgumentException(String.format("Found too many suitable implementations for %s", clazz));
		}

		return getSuitableConstructor(suitableClasses.get(0));
	}

	/**
	 * Returns an array of the given class.
	 * 
	 * @param clazz Class of which an array must be returned.
	 * @return An array of the given class.
	 */
	@SuppressWarnings("unchecked")
	private <T> T createArrayOf(Class<T> clazz) {
		return (T) Array.newInstance(clazz, 0);
	}

	/**
	 * Returns a new instance of the given class.
	 * 
	 * @param clazz Class of which a new instance must be returned.
	 * @return A new instance of the given class.
	 */
	private <T> T createNewInstanceOf(Class<T> clazz) {
		try {
			Constructor<? extends T> constructor = getSuitableConstructor(clazz);

			Object[] parameterInstances = resolveParametersOf(constructor);

			return constructor.newInstance(parameterInstances);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException(String.format("Cannot instantiate %s", clazz), e);
		}
	}

	/**
	 * Returns a proxy of the given class.
	 * 
	 * @param clazz Class of which a proxy must be returned.
	 * @return A proxy of the given class.
	 */
	@SuppressWarnings("unchecked")
	private <T> T createProxyOf(Class<T> clazz) {
		Constructor<? extends T> constructor = getSuitableConstructor(clazz);

		Object[] parameterInstances = resolveParametersOf(constructor);

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

		return (T) enhancer.create(constructor.getParameterTypes(), parameterInstances);
	}

}
