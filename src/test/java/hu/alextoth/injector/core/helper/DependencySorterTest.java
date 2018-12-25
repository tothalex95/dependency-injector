package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoInjectConstructor1;
import hu.alextoth.injector.demo.DemoInjectConstructor2;
import hu.alextoth.injector.demo.DemoInjectConstructor3;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableTwo;

public class DependencySorterTest {

	private static DependencySorter dependencySorter;

	@BeforeAll
	public static void setUp() {
		dependencySorter = new DependencySorter();
	}

	@Test
	public void testGetSortedInjectableMethods() throws NoSuchMethodException, SecurityException {
		List<Method> methods = Lists.newArrayList(ConfigClass.class.getDeclaredMethod("getNamedDemoInjectableOne"),
				ConfigClass.class.getDeclaredMethod("getDemoInjectableOne"),
				ConfigClass.class.getDeclaredMethod("getNamedDemoInjectableOne2"),
				ConfigClass.class.getDeclaredMethod("getDemoInjectableNine"));

		List<Method> sortedMethods = dependencySorter.getSortedInjectableMethods(Sets.newHashSet(methods));

		assertEquals(methods.size(), sortedMethods.size());

		for (int i = 0; i < sortedMethods.size(); i++) {
			assertEquals(methods.get(i), sortedMethods.get(i));
		}
	}

	@Test
	public void testGetSortedInjectConstructors() throws NoSuchMethodException, SecurityException {
		List<Constructor<?>> constructors = Lists.newArrayList(
				DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class),
				DemoInjectConstructor1.class.getDeclaredConstructor(DemoInjectableTwo.class),
				DemoInjectConstructor3.class.getDeclaredConstructor(DemoInjectConstructor1.class),
				DemoInjectConstructor2.class.getDeclaredConstructor(DemoInjectableTwo.class,
						DemoInjectConstructor3.class));

		List<Constructor<?>> sortedConstructors = dependencySorter
				.getSortedInjectConstructors(Sets.newHashSet(constructors));

		assertEquals(constructors.size(), sortedConstructors.size());

		for (int i = 0; i < sortedConstructors.size(); i++) {
			assertEquals(constructors.get(i), sortedConstructors.get(i));
		}
	}

}
