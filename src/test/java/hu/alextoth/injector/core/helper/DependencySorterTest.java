package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import hu.alextoth.injector.DependencyInjectorTest;
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

		List<Method> shuffledMethods = Lists.newArrayList(methods);
		Collections.shuffle(shuffledMethods);

		List<Method> sortedMethods = dependencySorter.getSortedInjectableMethods(Sets.newHashSet(shuffledMethods));

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

		List<Constructor<?>> shuffledConstructors = Lists.newArrayList(constructors);
		Collections.shuffle(shuffledConstructors);

		List<Constructor<?>> sortedConstructors = dependencySorter
				.getSortedInjectConstructors(Sets.newHashSet(shuffledConstructors));

		assertEquals(constructors.size(), sortedConstructors.size());

		for (int i = 0; i < sortedConstructors.size(); i++) {
			assertEquals(constructors.get(i), sortedConstructors.get(i));
		}
	}

	@Test
	public void testGetSortedInjectFields() throws NoSuchFieldException, SecurityException {
		List<Field> fields = Lists.newArrayList(DemoInjectConstructor3.class.getDeclaredField("demoInjectConstructor1"),
				DemoInjectConstructor2.class.getDeclaredField("demoInjectConstructor3"),
				DependencyInjectorTest.class.getDeclaredField("demoInjectConstructor2"));

		List<Field> shuffledFields = Lists.newArrayList(fields);
		Collections.shuffle(shuffledFields);

		List<Field> sortedFields = dependencySorter.getSortedInjectFields(Sets.newHashSet(shuffledFields));

		assertEquals(fields.size(), sortedFields.size());

		for (int i = 0; i < sortedFields.size(); i++) {
			assertEquals(fields.get(i), sortedFields.get(i));
		}
	}

	@Test
	public void testGetSortedInjectMethods() throws NoSuchMethodException, SecurityException {
		List<Method> methods = Lists.newArrayList(
				DemoInjectConstructor3.class.getDeclaredMethod("getDemoInjectConstructor1"),
				DemoInjectConstructor2.class.getDeclaredMethod("getDemoInjectConstructor3"));

		List<Method> shuffledMethods = Lists.newArrayList(methods);
		Collections.shuffle(shuffledMethods);

		List<Method> sortedMethods = dependencySorter.getSortedInjectMethods(Sets.newHashSet(shuffledMethods));

		assertEquals(methods.size(), sortedMethods.size());

		for (int i = 0; i < sortedMethods.size(); i++) {
			assertEquals(methods.get(i), sortedMethods.get(i));
		}
	}

}
