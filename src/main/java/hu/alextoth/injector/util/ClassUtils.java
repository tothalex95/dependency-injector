package hu.alextoth.injector.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class related utility methods.
 * 
 * @author Alex Toth
 */
public final class ClassUtils {

	private ClassUtils() {
	}

	private static final Map<Class<?>, Object> PRIMITIVE_DEFAULT_VALUE_MAP;

	static {
		Map<Class<?>, Object> primitiveDefaultValueMap = new HashMap<>(8);

		primitiveDefaultValueMap.put(boolean.class, false);
		primitiveDefaultValueMap.put(byte.class, (byte) 0);
		primitiveDefaultValueMap.put(char.class, (char) 0);
		primitiveDefaultValueMap.put(double.class, 0.0);
		primitiveDefaultValueMap.put(float.class, 0.0f);
		primitiveDefaultValueMap.put(int.class, 0);
		primitiveDefaultValueMap.put(long.class, 0L);
		primitiveDefaultValueMap.put(short.class, (short) 0);

		PRIMITIVE_DEFAULT_VALUE_MAP = Collections.unmodifiableMap(primitiveDefaultValueMap);
	}

	private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP;

	static {
		Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>(8);

		primitiveWrapperMap.put(boolean.class, Boolean.class);
		primitiveWrapperMap.put(byte.class, Byte.class);
		primitiveWrapperMap.put(char.class, Character.class);
		primitiveWrapperMap.put(double.class, Double.class);
		primitiveWrapperMap.put(float.class, Float.class);
		primitiveWrapperMap.put(int.class, Integer.class);
		primitiveWrapperMap.put(long.class, Long.class);
		primitiveWrapperMap.put(short.class, Short.class);

		PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(primitiveWrapperMap);

	}

	private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP;

	static {
		Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<>(8);

		wrapperPrimitiveMap.put(Boolean.class, boolean.class);
		wrapperPrimitiveMap.put(Byte.class, byte.class);
		wrapperPrimitiveMap.put(Character.class, char.class);
		wrapperPrimitiveMap.put(Double.class, double.class);
		wrapperPrimitiveMap.put(Float.class, float.class);
		wrapperPrimitiveMap.put(Integer.class, int.class);
		wrapperPrimitiveMap.put(Long.class, long.class);
		wrapperPrimitiveMap.put(Short.class, short.class);

		WRAPPER_PRIMITIVE_MAP = Collections.unmodifiableMap(wrapperPrimitiveMap);
	}

	/**
	 * Converts the given value to the given primitive type.
	 * 
	 * @param clazz The conversion's target primitive type.
	 * @param value The value to be converted.
	 * @return The value converted to the given primitive type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertToPrimitive(Class<T> clazz, String value) {
		if (clazz == null || value == null) {
			throw new IllegalArgumentException("The type and the value shouldn't be null.");
		}

		if (String.class.isAssignableFrom(clazz)) {
			return (T) value;
		}
		if (char.class.equals(clazz) || Character.class.equals(clazz)) {
			return (T) Character.valueOf(value.charAt(0));
		}

		Class<?> wrapper = getWrapperForPrimitive(clazz);
		if (wrapper == null) {
			throw new IllegalArgumentException(String.format("%s is not primitive type.", clazz.getName()));
		}

		try {
			Method valueOfMethod = wrapper.getDeclaredMethod("valueOf", String.class);
			valueOfMethod.setAccessible(true);
			return (T) valueOfMethod.invoke(null, value);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException(String.format("Cannot convert %s to %s.", value, clazz.getName()), e);
		}
	}

	/**
	 * Returns the default value of the given primitive type.
	 * 
	 * @param primitive Primitive type of which the default value must be returned.
	 * @return The default value of the given primitive type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDefaultValueForPrimitive(Class<T> primitive) {
		if (isWrapper(primitive)) {
			return (T) PRIMITIVE_DEFAULT_VALUE_MAP.get(getPrimitiveForWrapper(primitive));
		}
		return (T) PRIMITIVE_DEFAULT_VALUE_MAP.get(primitive);
	}

	/**
	 * Returns the appropriate primitive type for the given wrapper.
	 * 
	 * @param wrapper Wrapper class of which the primitive type must be returned.
	 * @return The appropriate primitive type for the given wrapper.
	 */
	public static Class<?> getPrimitiveForWrapper(Class<?> wrapper) {
		if (isPrimitive(wrapper)) {
			return wrapper;
		}
		return WRAPPER_PRIMITIVE_MAP.get(wrapper);
	}

	/**
	 * Returns the appropriate wrapper for the given primitive type.
	 * 
	 * @param primitive Primitive type of which the wrapper class must be returned.
	 * @return The appropriate wrapper for the given primitive type.
	 */
	public static Class<?> getWrapperForPrimitive(Class<?> primitive) {
		if (isWrapper(primitive)) {
			return primitive;
		}
		return PRIMITIVE_WRAPPER_MAP.get(primitive);
	}

	/**
	 * Returns a boolean value indicating whether the given class is concrete or
	 * not.
	 * 
	 * @param clazz Class to check whether it's concrete or not.
	 * @return A boolean value indicating whether the given class is concrete or
	 *         not.
	 */
	public static boolean isConcrete(Class<?> clazz) {
		return !(clazz.isPrimitive() || clazz.isInterface() || clazz.isAnnotation() || clazz.isArray() || clazz.isEnum()
				|| isWrapper(clazz) || Modifier.isAbstract(clazz.getModifiers()));
	}

	/**
	 * Returns a boolean value indicating whether the given class is a primitive
	 * type or not.
	 * 
	 * @param clazz Class to check whether it's a primitive type or not.
	 * @return A boolean value indicating whether the given class is a primitive
	 *         type or not.
	 */
	public static boolean isPrimitive(Class<?> clazz) {
		return clazz.isPrimitive();
	}

	/**
	 * Returns a boolean value indicating whether the given class is a primitive
	 * type or a wrapper class.
	 * 
	 * @param clazz Class to check whether it's a primitive type or a wrapper class.
	 * @return A boolean value indicating whether the given class is a primitive
	 *         type or a wrapper class.
	 */
	public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
		return isPrimitive(clazz) || isWrapper(clazz);
	}

	/**
	 * Returns a boolean value indicating whether the given class is a wrapper class
	 * or not.
	 * 
	 * @param clazz Class to check whether it's a wrapper class or not.
	 * @return A boolean value indicating whether the given class is a wrapper class
	 *         or not.
	 */
	public static boolean isWrapper(Class<?> clazz) {
		return WRAPPER_PRIMITIVE_MAP.containsKey(clazz);
	}

}
