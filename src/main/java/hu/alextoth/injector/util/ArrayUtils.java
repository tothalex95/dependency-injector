package hu.alextoth.injector.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtils {

	private ArrayUtils() {
	}

	/**
	 * Converts the given values to the given primitive type.
	 * 
	 * @param componentType The conversion's target primitive type.
	 * @param values        The values to be converted.
	 * @return The values converted to the given primitive type.
	 */
	public static Object convertToPrimitiveArray(Class<?> componentType, String... values) {
		if (componentType == null || values == null) {
			throw new IllegalArgumentException("The type and the values shouldn't be null.");
		}

		if (String.class.isAssignableFrom(componentType)) {
			return values;
		}

		Class<?> wrapper = ClassUtils.getWrapperForPrimitive(componentType);
		if (wrapper == null) {
			throw new IllegalArgumentException(String.format("%s is not a primitive data type.", componentType));
		}

		Object convertedValues = Array.newInstance(wrapper, values.length);
		Arrays.setAll((Object[]) convertedValues, i -> ClassUtils.convertToPrimitive(componentType, values[i]));

		return componentType.isPrimitive() ? convertToPrimitiveArray(componentType, convertedValues) : convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static boolean[] convertToPrimitiveArray(Boolean[] values) {
		if (values == null) {
			return null;
		}

		boolean[] convertedValues = new boolean[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].booleanValue();
		}

		return convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static byte[] convertToPrimitiveArray(Byte[] values) {
		if (values == null) {
			return null;
		}

		byte[] convertedValues = new byte[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].byteValue();
		}

		return convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static char[] convertToPrimitiveArray(Character[] values) {
		if (values == null) {
			return null;
		}

		char[] convertedValues = new char[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].charValue();
		}

		return convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static double[] convertToPrimitiveArray(Double[] values) {
		if (values == null) {
			return null;
		}

		double[] convertedValues = new double[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].doubleValue();
		}

		return convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static float[] convertToPrimitiveArray(Float[] values) {
		if (values == null) {
			return null;
		}

		float[] convertedValues = new float[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].floatValue();
		}

		return convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static int[] convertToPrimitiveArray(Integer[] values) {
		if (values == null) {
			return null;
		}

		int[] convertedValues = new int[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].intValue();
		}

		return convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static long[] convertToPrimitiveArray(Long[] values) {
		if (values == null) {
			return null;
		}

		long[] convertedValues = new long[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].longValue();
		}

		return convertedValues;
	}

	/**
	 * Converts the given array of wrapper type to an array of the appropriate
	 * primitive type.
	 * 
	 * @param values Values to be converted to the appropriate primitive type.
	 * @return An array of values converted to the appropriate primitive type.
	 */
	public static short[] convertToPrimitiveArray(Short[] values) {
		if (values == null) {
			return null;
		}

		short[] convertedValues = new short[values.length];
		for (int i = 0; i < convertedValues.length; i++) {
			convertedValues[i] = values[i].shortValue();
		}

		return convertedValues;
	}

	/**
	 * Returns a boolean value indicating whether the given class is an array of
	 * primitive type.
	 * 
	 * @param clazz Class to check whether it's a primitive array.
	 * @return A boolean value indicating whether the given class is an array of
	 *         primitive type.
	 */
	public static boolean isPrimitiveArray(Class<?> clazz) {
		return clazz != null && clazz.isArray() && clazz.getComponentType().isPrimitive();
	}

	/**
	 * Returns a boolean value indicating whether the given class is an array of
	 * primitive or wrapper type.
	 * 
	 * @param clazz Class to check whether it's a primitive or wrapper array.
	 * @return A boolean value indicating whether the given class is an array of
	 *         primitive or wrapper type.
	 */
	public static boolean isPrimitiveOrWrapperArray(Class<?> clazz) {
		return clazz != null && clazz.isArray() && ClassUtils.isPrimitiveOrWrapper(clazz.getComponentType());
	}

	/**
	 * Returns a boolean value indicating whether the given class is an array of
	 * wrapper type.
	 * 
	 * @param clazz Class to check whether it's a wrapper array.
	 * @return A boolean value indicating whether the given class is an array of
	 *         wrapper type.
	 */
	public static boolean isWrapperArray(Class<?> clazz) {
		return clazz != null && clazz.isArray() && ClassUtils.isWrapper(clazz.getComponentType());
	}

	/**
	 * Helper method to convert an array of wrapper type to primitive array.
	 * 
	 * @param componentType The target primitive data type.
	 * @param values        A wrapper array as an object.
	 * @return A primitive array as an object.
	 */
	private static Object convertToPrimitiveArray(Class<?> componentType, Object values) {
		if (boolean.class.equals(componentType)) {
			return convertToPrimitiveArray((Boolean[]) values);
		}
		if (byte.class.equals(componentType)) {
			return convertToPrimitiveArray((Byte[]) values);
		}
		if (char.class.equals(componentType)) {
			return convertToPrimitiveArray((Character[]) values);
		}
		if (double.class.equals(componentType)) {
			return convertToPrimitiveArray((Double[]) values);
		}
		if (float.class.equals(componentType)) {
			return convertToPrimitiveArray((Float[]) values);
		}
		if (int.class.equals(componentType)) {
			return convertToPrimitiveArray((Integer[]) values);
		}
		if (long.class.equals(componentType)) {
			return convertToPrimitiveArray((Long[]) values);
		}
		return convertToPrimitiveArray((Short[]) values);
	}

}
