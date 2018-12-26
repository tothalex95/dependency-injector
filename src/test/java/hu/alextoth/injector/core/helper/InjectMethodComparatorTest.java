package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import hu.alextoth.injector.core.MockitoExtension;
import hu.alextoth.injector.demo.DemoInjectConstructor2;
import hu.alextoth.injector.demo.DemoInjectConstructor3;

@ExtendWith(MockitoExtension.class)
public class InjectMethodComparatorTest {

	@Mock
	private DependencyComparator dependencyComparator;

	@InjectMocks
	private InjectMethodComparator injectMethodComparator;

	@Test
	public void testCompare() throws NoSuchMethodException, SecurityException {
		Mockito.when(dependencyComparator.compare(DemoInjectConstructor2.class, DemoInjectConstructor3.class))
				.thenReturn(1);
		Mockito.when(dependencyComparator.compare(DemoInjectConstructor3.class, DemoInjectConstructor2.class))
				.thenReturn(-1);

		assertEquals(1,
				injectMethodComparator.compare(
						DemoInjectConstructor2.class.getDeclaredMethod("getDemoInjectConstructor3"),
						DemoInjectConstructor3.class.getDeclaredMethod("getDemoInjectConstructor1")));
		assertEquals(-1,
				injectMethodComparator.compare(
						DemoInjectConstructor3.class.getDeclaredMethod("getDemoInjectConstructor1"),
						DemoInjectConstructor2.class.getDeclaredMethod("getDemoInjectConstructor3")));

		Mockito.verify(dependencyComparator).compare(DemoInjectConstructor2.class, DemoInjectConstructor3.class);
		Mockito.verify(dependencyComparator).compare(DemoInjectConstructor3.class, DemoInjectConstructor2.class);

		Mockito.reset(dependencyComparator);
	}

}
