package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reflections.Reflections;

import com.google.common.collect.Sets;

import hu.alextoth.injector.demo.DemoFinalClass;
import hu.alextoth.injector.demo.DemoInjectableEight;
import hu.alextoth.injector.demo.DemoInjectableFive;
import hu.alextoth.injector.demo.DemoInjectableFiveImpl;
import hu.alextoth.injector.demo.DemoInjectableFour;
import hu.alextoth.injector.demo.DemoInjectableFourImpl;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableSeven;
import hu.alextoth.injector.demo.DemoInjectableSevenImpl1;
import hu.alextoth.injector.demo.DemoInjectableSevenImpl2;
import hu.alextoth.injector.demo.DemoInjectableSix;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@ExtendWith(MockitoExtension.class)
public class DependencyHandlerTest {

	@Mock
	private Reflections reflections;

	@Mock
	private AnnotationProcessorHelper annotationProcessorHelper;

	@Mock
	private DependencyAliasResolver dependencyAliasResolver;

	@InjectMocks
	private DependencyHandler dependencyHandler;

	@AfterEach
	public void tearDown() {
		Mockito.reset(reflections, annotationProcessorHelper, dependencyAliasResolver);
	}

	@Test
	public void testGetInstanceOf() {
		DemoInjectableThree demoInjectableThree = dependencyHandler.getInstanceOf(DemoInjectableThree.class);

		assertNotNull(demoInjectableThree);
	}

	@Test
	public void testCreateInstanceOfWithDefaultConstructor() {
		DemoInjectableThree demoInjectableThree = dependencyHandler.createInstanceOf(DemoInjectableThree.class);

		assertNotNull(demoInjectableThree);
	}

	@Test
	public void testCreateInstanceOfWithParameterizedConstructor() throws NoSuchMethodException, SecurityException {
		Mockito.when(dependencyAliasResolver
				.getAlias(DemoInjectableTwo.class.getConstructor(DemoInjectableOne.class).getParameters()[0]))
				.thenReturn("alias1");

		DemoInjectableOne demoInjectableOne = new DemoInjectableOne(1995, "Alex");

		dependencyHandler.registerInstanceOf(DemoInjectableOne.class, demoInjectableOne, "alias1");

		DemoInjectableTwo demoInjectableTwo = dependencyHandler.createInstanceOf(DemoInjectableTwo.class);

		assertNotNull(demoInjectableTwo);

		Mockito.verify(dependencyAliasResolver)
				.getAlias(DemoInjectableTwo.class.getConstructor(DemoInjectableOne.class).getParameters()[0]);
	}

	@Test
	public void testCreateInstanceOfWithNonPublicConstructor() {
		assertThrows(IllegalArgumentException.class,
				() -> dependencyHandler.createInstanceOf(DemoInjectableEight.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateInstanceOfWithInterface() {
		Mockito.when(reflections.getSubTypesOf(DemoInjectableFour.class))
				.thenReturn(Sets.newHashSet(DemoInjectableFourImpl.class));

		DemoInjectableFour demoInjectableFour = dependencyHandler.createInstanceOf(DemoInjectableFour.class);

		assertNotNull(demoInjectableFour);

		Mockito.verify(reflections).getSubTypesOf(DemoInjectableFour.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateInstanceOfWithAbstractClass() {
		Mockito.when(reflections.getSubTypesOf(DemoInjectableFive.class))
				.thenReturn(Sets.newHashSet(DemoInjectableFiveImpl.class));

		DemoInjectableFive demoInjectableFive = dependencyHandler.createInstanceOf(DemoInjectableFive.class);

		assertNotNull(demoInjectableFive);

		Mockito.verify(reflections).getSubTypesOf(DemoInjectableFive.class);
	}

	@Test
	public void testCreateInstanceOfWithInterfaceWithoutSuitableImplementation() {
		Mockito.when(reflections.getSubTypesOf(DemoInjectableSix.class)).thenReturn(Sets.newHashSet());

		assertThrows(IllegalArgumentException.class, () -> dependencyHandler.createInstanceOf(DemoInjectableSix.class));

		Mockito.verify(reflections).getSubTypesOf(DemoInjectableSix.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateInstanceOfWithInterfaceWithTooManySuitableImplementation() {
		Mockito.when(reflections.getSubTypesOf(DemoInjectableSeven.class))
				.thenReturn(Sets.newHashSet(DemoInjectableSevenImpl1.class, DemoInjectableSevenImpl2.class));

		assertThrows(IllegalArgumentException.class,
				() -> dependencyHandler.createInstanceOf(DemoInjectableSeven.class));

		Mockito.verify(reflections).getSubTypesOf(DemoInjectableSeven.class);
	}

	@Test
	public void testCreateInstanceOfWithPrimitive() {
		assertEquals(Integer.valueOf(0), dependencyHandler.createInstanceOf(Integer.class));
	}

	@Test
	public void testCreateInstanceOfWithArray() {
		Object[] objectArray = dependencyHandler.createInstanceOf(Object[].class);

		assertNotNull(objectArray);
		assertEquals(0, objectArray.length);
	}

	@Test
	public void testCreateInstanceOfWithFinalClass() throws NoSuchMethodException, SecurityException {
		assertNotNull(dependencyHandler.createInstanceOf(String.class));

		Mockito.when(dependencyAliasResolver
				.getAlias(DemoFinalClass.class.getDeclaredConstructor(String.class).getParameters()[0]))
				.thenThrow(IllegalArgumentException.class);
		assertThrows(IllegalArgumentException.class, () -> dependencyHandler.createInstanceOf(DemoFinalClass.class));
		Mockito.verify(dependencyAliasResolver)
				.getAlias(DemoFinalClass.class.getDeclaredConstructor(String.class).getParameters()[0]);
	}

	@Test
	public void testRegisterInstanceOf() {
		DemoInjectableOne demoInjectableOne = new DemoInjectableOne(1995, "Alex");

		dependencyHandler.registerInstanceOf(DemoInjectableOne.class, demoInjectableOne);
		dependencyHandler.registerInstanceOf(DemoInjectableOne.class, demoInjectableOne, "alias");

		assertNotNull(dependencyHandler.getInstanceOf(DemoInjectableOne.class));
		assertEquals(dependencyHandler.getInstanceOf(DemoInjectableOne.class), demoInjectableOne);

		assertNotNull(dependencyHandler.getInstanceOf(DemoInjectableOne.class, "alias"));
		assertEquals(dependencyHandler.getInstanceOf(DemoInjectableOne.class, "alias"), demoInjectableOne);
	}

}
