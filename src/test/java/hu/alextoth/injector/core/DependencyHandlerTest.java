package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import hu.alextoth.injector.demo.DemoInjectableEight;
import hu.alextoth.injector.demo.DemoInjectableFive;
import hu.alextoth.injector.demo.DemoInjectableFour;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableSeven;
import hu.alextoth.injector.demo.DemoInjectableSix;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;
import hu.alextoth.injector.exception.DependencyCreationException;

class DependencyHandlerTest {

	private DependencyHandler dependencyHandler;

	@BeforeEach
	void setUp() {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(),
								new FieldAnnotationsScanner(), new MethodAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage("hu.alextoth.injector")));
		dependencyHandler = new DependencyHandler(reflections);
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
	void testCreateInstanceOfWithNonPublicConstructor() {
		assertThrows(DependencyCreationException.class,
				() -> dependencyHandler.createInstanceOf(DemoInjectableEight.class));
	}

	@Test
	void testCreateInstanceOfWithInterface() {
		DemoInjectableFour demoInjectableFour = dependencyHandler.createInstanceOf(DemoInjectableFour.class);

		assertNotNull(demoInjectableFour);
	}

	@Test
	void testCreateInstanceOfWithAbstractClass() {
		DemoInjectableFive demoInjectableFive = dependencyHandler.createInstanceOf(DemoInjectableFive.class);

		assertNotNull(demoInjectableFive);
	}

	@Test
	void testCreateInstanceOfWithInterfaceWithoutSuitableImplementation() {
		assertThrows(DependencyCreationException.class,
				() -> dependencyHandler.createInstanceOf(DemoInjectableSix.class));
	}

	@Test
	void testCreateInstanceOfWithInterfaceWithTooManySuitableImplementation() {
		assertThrows(DependencyCreationException.class,
				() -> dependencyHandler.createInstanceOf(DemoInjectableSeven.class));
	}

	@Test
	void testRegisterInstanceOf() {
		DemoInjectableOne demoInjectableOne = new DemoInjectableOne(1995, "Alex");

		dependencyHandler.registerInstanceOf(DemoInjectableOne.class, demoInjectableOne);

		assertNotNull(dependencyHandler.getInstanceOf(DemoInjectableOne.class));
		assertEquals(dependencyHandler.getInstanceOf(DemoInjectableOne.class), demoInjectableOne);
	}

}
