package hu.alextoth.injector.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import hu.alextoth.injector.demo.DemoInjectableFive;

public class ClassUtilsTest {

	@Test
	public void testConvertToPrimitive() {
		assertEquals(true, ClassUtils.convertToPrimitive(boolean.class, "true"));
		assertEquals(true, ClassUtils.convertToPrimitive(Boolean.class, "true"));

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
		assertEquals(true, ClassUtils.isConcrete(Object.class));
		assertEquals(false, ClassUtils.isConcrete(int.class));
		assertEquals(false, ClassUtils.isConcrete(Integer.class));
		assertEquals(false, ClassUtils.isConcrete(Comparable.class));
		assertEquals(false, ClassUtils.isConcrete(DemoInjectableFive.class));
		assertEquals(false, ClassUtils.isConcrete(Test.class));
		assertEquals(false, ClassUtils.isConcrete(Object[].class));
		assertEquals(false, ClassUtils.isConcrete(DemoEnum.class));
	}

	@Test
	public void testIsPrimitive() {
		assertEquals(true, ClassUtils.isPrimitive(int.class));
		assertEquals(false, ClassUtils.isPrimitive(Integer.class));
		assertEquals(false, ClassUtils.isPrimitive(Object.class));
	}

	@Test
	public void testIsPrimitiveOrWrapper() {
		assertEquals(true, ClassUtils.isPrimitiveOrWrapper(int.class));
		assertEquals(true, ClassUtils.isPrimitiveOrWrapper(Integer.class));
		assertEquals(false, ClassUtils.isPrimitiveOrWrapper(Object.class));
	}

	@Test
	public void testIsWrapper() {
		assertEquals(false, ClassUtils.isWrapper(int.class));
		assertEquals(true, ClassUtils.isWrapper(Integer.class));
		assertEquals(false, ClassUtils.isWrapper(Object.class));
	}

	private enum DemoEnum {
		DEMO
	}

}
