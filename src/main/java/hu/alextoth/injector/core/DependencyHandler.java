package hu.alextoth.injector.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import hu.alextoth.injector.exception.DependencyCreationException;

/**
 * Class for managing instances of dependency classes.
 * 
 * @author Alex Toth
 */
public class DependencyHandler {

	private final Reflections reflections;
	private final Map<Class<?>, Object> dependencies;

	public DependencyHandler(Reflections reflections) {
		this.reflections = reflections;
		dependencies = new HashMap<>();
	}

	/**
	 * Returns, or creates if necessary, an instance of the given class.
	 * 
	 * @param clazz Class of which an instance must be returned.
	 * @return A registered instance of the given class.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstanceOf(Class<T> clazz) {
		T instance = (T) dependencies.get(clazz);

		if (instance == null) {
			instance = createInstanceOf(clazz);
		}

		return instance;
	}

	/**
	 * Creates and registers an instance of the given class.<br>
	 * Returns the created instance.
	 * 
	 * @param clazz Class to be instantiated.
	 * @return An instance of the given class.
	 */
	public <T> T createInstanceOf(Class<T> clazz) {
		T instance = null;

		Constructor<T> constructor = getSuitableConstructor(clazz);

		constructor.setAccessible(true);
		Class<?>[] parameterClasses = constructor.getParameterTypes();
		Object[] parameterInstances = new Object[parameterClasses.length];
		for (int i = 0; i < parameterClasses.length; i++) {
			parameterInstances[i] = getInstanceOf(parameterClasses[i]);
		}

		try {
			instance = constructor.newInstance(parameterInstances);
			registerInstanceOf(clazz, instance);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new DependencyCreationException(String.format("Cannot instantiate %s", clazz), e);
		}

		return instance;
	}

	/**
	 * Registers an instance for the given class.
	 * 
	 * @param clazz    Class for which the instance must be registered.
	 * @param instance Class instance to be registered.
	 */
	public void registerInstanceOf(Class<?> clazz, Object instance) {
		dependencies.put(clazz, instance);
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
	private <T> Constructor<T> getSuitableConstructor(Class<T> clazz) {
		Constructor<T> constructor = null;
		try {
			constructor = clazz.getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			Constructor<T>[] declaredConstuctors = (Constructor<T>[]) clazz.getDeclaredConstructors();
			if (declaredConstuctors.length == 0) {
				// TODO: handle interfaces
			} else {
				constructor = declaredConstuctors[0];
			}
		}
		return constructor;
	}

}
