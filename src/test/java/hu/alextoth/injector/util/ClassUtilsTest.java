package hu.alextoth.injector.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hu.alextoth.injector.demo.DemoInjectableFive;

public class ClassUtilsTest {

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