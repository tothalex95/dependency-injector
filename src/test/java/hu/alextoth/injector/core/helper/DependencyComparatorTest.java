package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hu.alextoth.injector.demo.DemoInjectableFourImpl;
import hu.alextoth.injector.demo.DemoInjectableNine;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableTwo;

public class DependencyComparatorTest {

	@Test
	public void testCompare() {
		DependencyComparator dependencyComparator = new DependencyComparator();

		assertEquals(-1, dependencyComparator.compare(DemoInjectableOne.class, DemoInjectableNine.class));
		assertEquals(1, dependencyComparator.compare(DemoInjectableTwo.class, DemoInjectableOne.class));
		assertEquals(0, dependencyComparator.compare(DemoInjectableTwo.class, DemoInjectableFourImpl.class));
	}

}
