package hu.alextoth.injector.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArrayUtilsTest {

	@Test
	public void testConvertToPrimitiveArray() {
		String[] strings = { "a", "b", "c" };
		assertEquals(strings, ArrayUtils.convertToPrimitiveArray(String.class, strings));

		char[] chars = (char[]) ArrayUtils.convertToPrimitiveArray(char.class, strings);
		for (int i = 0; i < chars.length; i++) {
			assertEquals(strings[i], Character.toString(chars[i]));
		}

		String[] integerValues = { "1", "2", "3" };
		Integer[] integers = (Integer[]) ArrayUtils.convertToPrimitiveArray(Integer.class, integerValues);
		for (int i = 0; i < integers.length; i++) {
			assertEquals(integerValues[i], Integer.toString(integers[i]));
		}

		byte[] bytes = (byte[]) ArrayUtils.convertToPrimitiveArray(byte.class, integerValues);
		for (int i = 0; i < bytes.length; i++) {
			assertEquals(integerValues[i], Byte.toString(bytes[i]));
		}

		int[] ints = (int[]) ArrayUtils.convertToPrimitiveArray(int.class, integerValues);
		for (int i = 0; i < ints.length; i++) {
			assertEquals(integerValues[i], Integer.toString(ints[i]));
		}

		long[] longs = (long[]) ArrayUtils.convertToPrimitiveArray(long.class, integerValues);
		for (int i = 0; i < longs.length; i++) {
			assertEquals(integerValues[i], Long.toString(longs[i]));
		}

		short[] shorts = (short[]) ArrayUtils.convertToPrimitiveArray(short.class, integerValues);
		for (int i = 0; i < shorts.length; i++) {
			assertEquals(integerValues[i], Short.toString(shorts[i]));
		}

		String[] floatValues = { "1.0", "2.0", "3.0" };
		double[] doubles = (double[]) ArrayUtils.convertToPrimitiveArray(double.class, floatValues);
		for (int i = 0; i < doubles.length; i++) {
			assertEquals(floatValues[i], Double.toString(doubles[i]));
		}

		float[] floats = (float[]) ArrayUtils.convertToPrimitiveArray(float.class, floatValues);
		for (int i = 0; i < floats.length; i++) {
			assertEquals(floatValues[i], Float.toString(floats[i]));
		}

		String[] booleanValues = { "true", "false", "true" };
		boolean[] booleans = (boolean[]) ArrayUtils.convertToPrimitiveArray(boolean.class, booleanValues);
		for (int i = 0; i < booleans.length; i++) {
			assertEquals(booleanValues[i], Boolean.toString(booleans[i]));
		}

		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.convertToPrimitiveArray(null, null));
		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.convertToPrimitiveArray(int.class, null));
		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.convertToPrimitiveArray(null, ""));
		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.convertToPrimitiveArray(Object.class, ""));

		assertNull(ArrayUtils.convertToPrimitiveArray((Boolean[]) null));
		assertNull(ArrayUtils.convertToPrimitiveArray((Byte[]) null));
		assertNull(ArrayUtils.convertToPrimitiveArray((Character[]) null));
		assertNull(ArrayUtils.convertToPrimitiveArray((Double[]) null));
		assertNull(ArrayUtils.convertToPrimitiveArray((Float[]) null));
		assertNull(ArrayUtils.convertToPrimitiveArray((Integer[]) null));
		assertNull(ArrayUtils.convertToPrimitiveArray((Long[]) null));
		assertNull(ArrayUtils.convertToPrimitiveArray((Short[]) null));
	}

	@Test
	public void testIsPrimitiveArray() {
		assertTrue(ArrayUtils.isPrimitiveArray(int[].class));
		assertFalse(ArrayUtils.isPrimitiveArray(Integer[].class));
		assertFalse(ArrayUtils.isPrimitiveArray(Object[].class));
		assertFalse(ArrayUtils.isPrimitiveArray(null));
		assertFalse(ArrayUtils.isPrimitiveArray(byte.class));
	}

	@Test
	public void testIsPrimitiveOrWrapperArray() {
		assertTrue(ArrayUtils.isPrimitiveOrWrapperArray(int[].class));
		assertTrue(ArrayUtils.isPrimitiveOrWrapperArray(Integer[].class));
		assertFalse(ArrayUtils.isPrimitiveOrWrapperArray(Object[].class));
		assertFalse(ArrayUtils.isPrimitiveOrWrapperArray(null));
		assertFalse(ArrayUtils.isPrimitiveOrWrapperArray(byte.class));
	}

	@Test
	public void testIsWrapperArray() {
		assertFalse(ArrayUtils.isWrapperArray(int[].class));
		assertTrue(ArrayUtils.isWrapperArray(Integer[].class));
		assertFalse(ArrayUtils.isWrapperArray(Object[].class));
		assertFalse(ArrayUtils.isWrapperArray(null));
		assertFalse(ArrayUtils.isWrapperArray(byte.class));
	}

}
