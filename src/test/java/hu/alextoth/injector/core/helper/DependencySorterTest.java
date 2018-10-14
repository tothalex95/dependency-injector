package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import hu.alextoth.injector.demo.ConfigClass;

public class DependencySorterTest {

	@Test
	public void testGetSortedInjectableMethods() throws NoSuchMethodException, SecurityException {
		DependencySorter dependencySorter = new DependencySorter();

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

}
