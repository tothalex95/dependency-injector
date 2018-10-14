package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import hu.alextoth.injector.core.MockitoExtension;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@ExtendWith(MockitoExtension.class)
public class InjectableMethodComparatorTest {

	@Mock
	private DependencyComparator dependencyComparator;

	@InjectMocks
	private InjectableMethodComparator injectableMethodComparator;

	@Test
	public void testCompare() throws NoSuchMethodException, SecurityException {
		Mockito.when(dependencyComparator.compare(DemoInjectableOne.class, DemoInjectableTwo.class)).thenReturn(1);
		Mockito.when(dependencyComparator.compare(DemoInjectableTwo.class, DemoInjectableOne.class)).thenReturn(-1);
		Mockito.when(dependencyComparator.compare(DemoInjectableOne.class, DemoInjectableThree.class)).thenReturn(0);
		Mockito.when(dependencyComparator.compare(DemoInjectableTwo.class, DemoInjectableThree.class)).thenReturn(0);

		assertEquals(1,
				injectableMethodComparator.compare(
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableOne"),
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableTwo")));

		assertEquals(-1,
				injectableMethodComparator.compare(
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableTwo"),
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableOne")));

		assertEquals(0,
				injectableMethodComparator.compare(
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableOne"),
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableThree")));

		assertEquals(0,
				injectableMethodComparator.compare(
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableTwo"),
						InjectableMethodComparatorTest.class.getDeclaredMethod("demoInjectableThree")));

		Mockito.verify(dependencyComparator).compare(DemoInjectableOne.class, DemoInjectableTwo.class);
		Mockito.verify(dependencyComparator).compare(DemoInjectableTwo.class, DemoInjectableOne.class);
		Mockito.verify(dependencyComparator).compare(DemoInjectableOne.class, DemoInjectableThree.class);
		Mockito.verify(dependencyComparator).compare(DemoInjectableTwo.class, DemoInjectableThree.class);

		Mockito.reset(dependencyComparator);
	}

	public DemoInjectableOne demoInjectableOne() {
		return null;
	}

	public DemoInjectableTwo demoInjectableTwo() {
		return null;
	}

	public DemoInjectableThree demoInjectableThree() {
		return null;
	}

}
