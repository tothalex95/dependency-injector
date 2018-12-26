package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import hu.alextoth.injector.DependencyInjectorTest;
import hu.alextoth.injector.core.MockitoExtension;
import hu.alextoth.injector.demo.DemoInjectConstructor2;
import hu.alextoth.injector.demo.DemoInjectConstructor3;

@ExtendWith(MockitoExtension.class)
public class InjectFieldComparatorTest {

	@Mock
	private DependencyComparator dependencyComparator;

	@InjectMocks
	private InjectFieldComparator injectFieldComparator;

	@Test
	public void testCompare() throws NoSuchFieldException, SecurityException {
		Mockito.when(dependencyComparator.compare(DependencyInjectorTest.class, DemoInjectConstructor2.class))
				.thenReturn(1);
		Mockito.when(dependencyComparator.compare(DependencyInjectorTest.class, DemoInjectConstructor3.class))
				.thenReturn(1);
		Mockito.when(dependencyComparator.compare(DemoInjectConstructor2.class, DependencyInjectorTest.class))
				.thenReturn(-1);
		Mockito.when(dependencyComparator.compare(DemoInjectConstructor3.class, DependencyInjectorTest.class))
				.thenReturn(-1);

		assertEquals(1,
				injectFieldComparator.compare(DependencyInjectorTest.class.getDeclaredField("demoInjectConstructor2"),
						DemoInjectConstructor2.class.getDeclaredField("demoInjectConstructor3")));
		assertEquals(1,
				injectFieldComparator.compare(DependencyInjectorTest.class.getDeclaredField("demoInjectConstructor3"),
						DemoInjectConstructor3.class.getDeclaredField("demoInjectConstructor1")));
		assertEquals(-1,
				injectFieldComparator.compare(DemoInjectConstructor2.class.getDeclaredField("demoInjectConstructor3"),
						DependencyInjectorTest.class.getDeclaredField("demoInjectConstructor2")));
		assertEquals(-1,
				injectFieldComparator.compare(DemoInjectConstructor3.class.getDeclaredField("demoInjectConstructor1"),
						DependencyInjectorTest.class.getDeclaredField("demoInjectConstructor3")));

		Mockito.verify(dependencyComparator).compare(DependencyInjectorTest.class, DemoInjectConstructor2.class);
		Mockito.verify(dependencyComparator).compare(DependencyInjectorTest.class, DemoInjectConstructor3.class);
		Mockito.verify(dependencyComparator).compare(DemoInjectConstructor2.class, DependencyInjectorTest.class);
		Mockito.verify(dependencyComparator).compare(DemoInjectConstructor3.class, DependencyInjectorTest.class);

		Mockito.reset(dependencyComparator);
	}

}
