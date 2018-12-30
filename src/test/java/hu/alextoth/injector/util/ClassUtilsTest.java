package hu.alextoth.injector.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hu.alextoth.injector.demo.DemoInjectableFive;

public class ClassUtilsTest {

	@Test
	public void testConvertToPrimitive() {
		assertTrue(ClassUtils.convertToPrimitive(boolean.class, "true"));
		assertTrue(ClassUtils.convertToPrimitive(Boolean.class, "true"));

		assertEquals(Byte.valueOf((byte) 20), ClassUtils.convertToPrimitive(byte.class, "20"));
		assertEquals(Byte.valueOf((byte) 20), ClassUtils.convertToPrimitive(Byte.class, "20"));

		assertEquals(Character.valueOf('A'), ClassUtils.convertToPrimitive(char.class, "A"));
		assertEquals(Character.valueOf('A'), ClassUtils.convertToPrimitive(Character.class, "A"));

		assertEquals(Double.valueOf(2018.1022), ClassUtils.convertToPrimitive(double.class, "2018.1022"));
		assertEquals(Double.valueOf(2018.1022), ClassUtils.convertToPrimitive(Double.class, "2018.1022"));

		assertEquals(Float.valueOf(2018.1022f), ClassUtils.convertToPrimitive(float.class, "2018.1022f"));
		assertEquals(Float.valueOf(2018.1022f), ClassUtils.convertToPrimitive(Float.class, "2018.1022f"));

		assertEquals(Integer.valueOf(2018), ClassUtils.convertToPrimitive(int.class, "2018"));
		assertEquals(Integer.valueOf(2018), ClassUtils.convertToPrimitive(Integer.class, "2018"));

		assertEquals(Long.valueOf(2018L), ClassUtils.convertToPrimitive(long.class, "2018"));
		assertEquals(Long.valueOf(2018L), ClassUtils.convertToPrimitive(Long.class, "2018"));

		assertEquals(Short.valueOf((short) 2018), ClassUtils.convertToPrimitive(short.class, "2018"));
		assertEquals(Short.valueOf((short) 2018), ClassUtils.convertToPrimitive(Short.class, "2018"));

		assertEquals("2018", ClassUtils.convertToPrimitive(String.class, "2018"));

		assertThrows(IllegalArgumentException.class, () -> ClassUtils.convertToPrimitive(Object.class, ""));
		assertThrows(IllegalArgumentException.class, () -> ClassUtils.convertToPrimitive(int.class, "ABC"));
		assertThrows(IllegalArgumentException.class, () -> ClassUtils.convertToPrimitive(null, "value"));
		assertThrows(IllegalArgumentException.class, () -> ClassUtils.convertToPrimitive(Object.class, null));
	}

	@Test
	public void testGetDefaultValueForPrimitive() {
		long defaultLongValue = ClassUtils.getDefaultValueForPrimitive(long.class);
		assertEquals(0L, defaultLongValue);

		int defaultIntegerValue = ClassUtils.getDefaultValueForPrimitive(Integer.class);
		assertEquals(0, defaultIntegerValue);

		assertEquals(null, ClassUtils.getDefaultValueForPrimitive(Object.class));
	}

	@Test
	public void testGetPrimitiveForWrapper() {
		assertEquals(int.class, ClassUtils.getPrimitiveForWrapper(Integer.class));

		assertEquals(boolean.class, ClassUtils.getPrimitiveForWrapper(boolean.class));

		assertEquals(null, ClassUtils.getPrimitiveForWrapper(Object.class));
	}

	@Test
	public void testGetWrapperForPrimitive() {
		assertEquals(Integer.class, ClassUtils.getWrapperForPrimitive(int.class));

		assertEquals(Boolean.class, ClassUtils.getWrapperForPrimitive(Boolean.class));

		assertEquals(null, ClassUtils.getWrapperForPrimitive(Object.class));
	}

	@Test
	public void testIsConcrete() {
		assertTrue(ClassUtils.isConcrete(Object.class));
		assertFalse(ClassUtils.isConcrete(int.class));
		assertFalse(ClassUtils.isConcrete(Integer.class));
		assertFalse(ClassUtils.isConcrete(Comparable.class));
		assertFalse(ClassUtils.isConcrete(DemoInjectableFive.class));
		assertFalse(ClassUtils.isConcrete(Test.class));
		assertFalse(ClassUtils.isConcrete(Object[].class));
		assertFalse(ClassUtils.isConcrete(DemoEnum.class));
		assertFalse(ClassUtils.isConcrete(null));
	}

	@Test
	public void testIsPrimitive() {
		assertTrue(ClassUtils.isPrimitive(int.class));
		assertFalse(ClassUtils.isPrimitive(Integer.class));
		assertFalse(ClassUtils.isPrimitive(Object.class));
		assertFalse(ClassUtils.isPrimitive(null));
	}

	@Test
	public void testIsPrimitiveOrWrapper() {
		assertTrue(ClassUtils.isPrimitiveOrWrapper(int.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Integer.class));
		assertFalse(ClassUtils.isPrimitiveOrWrapper(Object.class));
	}

	@Test
	public void testIsWrapper() {
		assertFalse(ClassUtils.isWrapper(int.class));
		assertTrue(ClassUtils.isWrapper(Integer.class));
		assertFalse(ClassUtils.isWrapper(Object.class));
	}

	private enum DemoEnum {
		DEMO
	}

}
