package hu.alextoth.injector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@DemoAnnotation
public class DependencyInjectorTest {

	@Inject
	private static DemoInjectableOne demoInjectableOne;

	@Inject
	private static DemoInjectableTwo demoInjectableTwo;

	@Inject
	private static DemoInjectableThree demoInjectableThree;

	private DependencyInjector dependencyInjector;

	@BeforeEach
	public void setUp() throws Exception {
		dependencyInjector = new DependencyInjector("hu.alextoth.injector");
		dependencyInjector.injectDependencies();
	}

	@Test
	public void testInjectDependencies() {
		assertNotNull(demoInjectableOne);
		assertNotNull(demoInjectableTwo);
		assertNotNull(demoInjectableThree);

		assertEquals(demoInjectableOne.getDemoInteger(), Integer.valueOf(2018));
		assertEquals(demoInjectableOne.getDemoString(), "Alex Toth");

//		assertEquals(demoInjectableOne, demoInjectableTwo.getDemoInjectableOne());
		assertEquals(demoInjectableTwo, demoInjectableThree.getDemoInjectableTwo());
	}

	@Test
	public void testGetInstanceOf() {
		assertNotNull(dependencyInjector.getInstanceOf(Object.class));
		assertEquals(demoInjectableOne, dependencyInjector.getInstanceOf(DemoInjectableOne.class));
	}

}
