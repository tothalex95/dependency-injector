package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import hu.alextoth.injector.core.MockitoExtension;
import hu.alextoth.injector.demo.DemoInjectConstructor1;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@ExtendWith(MockitoExtension.class)
public class InjectConstructorComparatorTest {

	@Mock
	private DependencyComparator dependencyComparator;

	@InjectMocks
	private InjectConstructorComparator injectConstructorComparator;

	@Test
	public void testCompare() throws NoSuchMethodException, SecurityException {
		Mockito.when(dependencyComparator.compare(DemoInjectableTwo.class, DemoInjectConstructor1.class))
				.thenReturn(-1);
		Mockito.when(dependencyComparator.compare(DemoInjectConstructor1.class, DemoInjectableTwo.class)).thenReturn(1);

		assertEquals(-1,
				injectConstructorComparator.compare(
						DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class),
						DemoInjectConstructor1.class.getDeclaredConstructor(DemoInjectableTwo.class)));
		assertEquals(1,
				injectConstructorComparator.compare(
						DemoInjectConstructor1.class.getDeclaredConstructor(DemoInjectableTwo.class),
						DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class)));

		Mockito.verify(dependencyComparator).compare(DemoInjectableTwo.class, DemoInjectConstructor1.class);
		Mockito.verify(dependencyComparator).compare(DemoInjectConstructor1.class, DemoInjectableTwo.class);

		Mockito.reset(dependencyComparator);
	}

}
