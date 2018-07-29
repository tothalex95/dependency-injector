package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

class DependencyHandlerTest {

	private DependencyHandler dependencyHandler;

	@BeforeEach
	void setUp() {
		dependencyHandler = new DependencyHandler();
	}

	@Test
	void testGetInstanceOf() {
		DemoInjectableThree demoInjectableThree = dependencyHandler.getInstanceOf(DemoInjectableThree.class);

		assertNotNull(demoInjectableThree);
	}

	@Test
	void testCreateInstanceOfWithDefaultConstructor() {
		DemoInjectableThree demoInjectableThree = dependencyHandler.createInstanceOf(DemoInjectableThree.class);

		assertNotNull(demoInjectableThree);
	}

	@Test
	void testCreateInstanceOfWithParameterizedConstructor() {
		DemoInjectableOne demoInjectableOne = new DemoInjectableOne(1995, "Alex");

		dependencyHandler.registerInstanceOf(DemoInjectableOne.class, demoInjectableOne);
		
		DemoInjectableTwo demoInjectableTwo = dependencyHandler.createInstanceOf(DemoInjectableTwo.class);

		assertNotNull(demoInjectableTwo);
	}

	@Test
	void testRegisterInstanceOf() {
		DemoInjectableOne demoInjectableOne = new DemoInjectableOne(1995, "Alex");

		dependencyHandler.registerInstanceOf(DemoInjectableOne.class, demoInjectableOne);

		assertNotNull(dependencyHandler.getInstanceOf(DemoInjectableOne.class));
		assertEquals(dependencyHandler.getInstanceOf(DemoInjectableOne.class), demoInjectableOne);
	}

}
