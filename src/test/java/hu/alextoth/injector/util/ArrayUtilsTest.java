package hu.alextoth.injector.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.convertToPrimitiveArray(null, (String[]) null));
		assertThrows(IllegalArgumentException.class,
				() -> ArrayUtils.convertToPrimitiveArray(int.class, (String[]) null));
		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.convertToPrimitiveArray(null, ""));
		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.convertToPrimitiveArray(Object.class, ""));

		booleans = ArrayUtils.convertToPrimitiveArray((Boolean[]) null);
		assertNotNull(booleans);
		assertEquals(0, booleans.length);

		booleans = ArrayUtils.convertToPrimitiveArray(new Boolean[0]);
		assertNotNull(booleans);
		assertEquals(0, booleans.length);

		bytes = ArrayUtils.convertToPrimitiveArray((Byte[]) null);
		assertNotNull(bytes);
		assertEquals(0, bytes.length);

		bytes = ArrayUtils.convertToPrimitiveArray(new Byte[0]);
		assertNotNull(bytes);
		assertEquals(0, bytes.length);

		chars = ArrayUtils.convertToPrimitiveArray((Character[]) null);
		assertNotNull(chars);
		assertEquals(0, chars.length);

		chars = ArrayUtils.convertToPrimitiveArray(new Character[0]);
		assertNotNull(chars);
		assertEquals(0, chars.length);

		doubles = ArrayUtils.convertToPrimitiveArray((Double[]) null);
		assertNotNull(doubles);
		assertEquals(0, doubles.length);

		doubles = ArrayUtils.convertToPrimitiveArray(new Double[0]);
		assertNotNull(doubles);
		assertEquals(0, doubles.length);

		floats = ArrayUtils.convertToPrimitiveArray((Float[]) null);
		assertNotNull(floats);
		assertEquals(0, floats.length);

		floats = ArrayUtils.convertToPrimitiveArray(new Float[0]);
		assertNotNull(floats);
		assertEquals(0, floats.length);

		ints = ArrayUtils.convertToPrimitiveArray((Integer[]) null);
		assertNotNull(ints);
		assertEquals(0, ints.length);

		ints = ArrayUtils.convertToPrimitiveArray(new Integer[0]);
		assertNotNull(ints);
		assertEquals(0, ints.length);

		longs = ArrayUtils.convertToPrimitiveArray((Long[]) null);
		assertNotNull(longs);
		assertEquals(0, longs.length);

		longs = ArrayUtils.convertToPrimitiveArray(new Long[0]);
		assertNotNull(longs);
		assertEquals(0, longs.length);

		shorts = ArrayUtils.convertToPrimitiveArray((Short[]) null);
		assertNotNull(shorts);
		assertEquals(0, shorts.length);

		shorts = ArrayUtils.convertToPrimitiveArray(new Short[0]);
		assertNotNull(shorts);
		assertEquals(0, shorts.length);
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
