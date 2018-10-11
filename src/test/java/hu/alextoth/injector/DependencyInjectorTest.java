package hu.alextoth.injector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoInjectableNine;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@DemoAnnotation
public class DependencyInjectorTest {

	@Inject
	@Alias("alias1")
	private static DemoInjectableOne demoInjectableOne;

	@Inject
	private static DemoInjectableTwo demoInjectableTwo;

	@Inject
	private static DemoInjectableThree demoInjectableThree;

	@Inject
	private static DemoInjectableNine demoInjectableNine;

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
		assertNotNull(demoInjectableNine);

		assertEquals(demoInjectableOne.getDemoInteger(), Integer.valueOf(2018));
		assertEquals(demoInjectableOne.getDemoString(), "namedDependency");

		assertEquals(demoInjectableOne, demoInjectableTwo.getDemoInjectableOne());
		assertEquals(demoInjectableTwo, demoInjectableThree.getDemoInjectableTwo());

		assertEquals(demoInjectableOne, demoInjectableNine.getDemoInjectableOne());
	}

	@Test
	public void testGetInstanceOf() {
		assertNotNull(dependencyInjector.getDependency(Object.class, Alias.DEFAULT_ALIAS));
		assertEquals(demoInjectableOne, dependencyInjector.getDependency(DemoInjectableOne.class, "alias1"));
	}

}
