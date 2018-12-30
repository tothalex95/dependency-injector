package hu.alextoth.injector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Value;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoInjectConstructor2;
import hu.alextoth.injector.demo.DemoInjectConstructor3;
import hu.alextoth.injector.demo.DemoInjectableFour;
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

	@DemoAnnotation(name = "nine")
	private static DemoInjectableNine demoInjectableNine2;

	@Inject
	@Alias("getShort")
	private static short demoShort;

	@Inject
	private static long demoLong;

	@Inject
	private static String demoString;

	@Inject
	private static byte[] demoPrimitiveArray;

	@Inject
	private static Object[] demoObjectArray;

	@Value("20181225")
	private static int demoValue;

	@Inject
	private static DemoInjectConstructor2 demoInjectConstructor2;

	@Inject
	private static DemoInjectConstructor3 demoInjectConstructor3;

	@Value({ "2018", "12", "26" })
	private static short[] demoPrimitiveValueArray;

	@Value({ "20.18", "12.27" })
	private static Double[] demoWrapperValueArray;

	@Value({ "Hello", "World" })
	private static String[] demoStringValueArray;

	@DemoAnnotation
	private static DemoInjectableFour demoInjectableFour1;

	@DemoAnnotation
	private static DemoInjectableFour demoInjectableFour2;

	@Inject
	@Alias("staticOne")
	private static DemoInjectableOne staticDemoInjectableOne;

	@Inject
	@Alias("staticNine")
	private static DemoInjectableNine staticDemoInjectableNine;

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
		assertNotNull(demoInjectableNine2);

		assertEquals(Integer.valueOf(2018), demoInjectableOne.getDemoInteger());
		assertEquals("namedDependency", demoInjectableOne.getDemoString());

		assertEquals(demoInjectableOne, demoInjectableTwo.getDemoInjectableOne());
		assertEquals(demoInjectableTwo, demoInjectableThree.getDemoInjectableTwo());

		assertEquals(demoInjectableOne, demoInjectableNine.getDemoInjectableOne());

		assertEquals(demoInjectableOne, demoInjectableNine2.getDemoInjectableOne());

		assertEquals((short) 2018, demoShort);
		assertEquals(0L, demoLong);
		assertEquals("", demoString);

		assertNotNull(demoPrimitiveArray);
		assertNotNull(demoObjectArray);
		assertEquals(0, demoPrimitiveArray.length);
		assertEquals(0, demoObjectArray.length);

		assertEquals(20181225, demoValue);

		assertNotNull(demoInjectConstructor2);
		assertNotNull(demoInjectConstructor3);
		assertEquals(demoInjectConstructor3, demoInjectConstructor2.getDemoInjectConstructor3());

		assertNotNull(demoPrimitiveValueArray);
		assertEquals(3, demoPrimitiveValueArray.length);
		assertEquals(2018, demoPrimitiveValueArray[0]);
		assertEquals(12, demoPrimitiveValueArray[1]);
		assertEquals(26, demoPrimitiveValueArray[2]);

		assertNotNull(demoWrapperValueArray);
		assertEquals(2, demoWrapperValueArray.length);
		assertEquals(Double.valueOf(20.18), demoWrapperValueArray[0]);
		assertEquals(Double.valueOf(12.27), demoWrapperValueArray[1]);

		assertNotNull(demoStringValueArray);
		assertEquals(2, demoStringValueArray.length);
		assertEquals("Hello", demoStringValueArray[0]);
		assertEquals("World", demoStringValueArray[1]);

		assertNotNull(demoInjectableFour1);
		assertNotNull(demoInjectableFour2);
		assertEquals(demoInjectableFour1, demoInjectableFour2);

		assertNotNull(staticDemoInjectableOne);
		assertNotNull(staticDemoInjectableNine);
		assertNotNull(staticDemoInjectableNine.getDemoInjectableOne());
		assertNotEquals(staticDemoInjectableOne, staticDemoInjectableNine.getDemoInjectableOne());
	}

	@Test
	public void testGetInstanceOf() {
		assertNotNull(dependencyInjector.getDependency(Object.class, Alias.DEFAULT_ALIAS));
		assertEquals(demoInjectableOne, dependencyInjector.getDependency(DemoInjectableOne.class, "alias1"));
		assertTrue(dependencyInjector.getDependency(DemoInjectableOne.class, Alias.DEFAULT_ALIAS).isDemoBoolean());
		assertEquals(Integer.valueOf(20181230),
				dependencyInjector.getDependency(DemoInjectableOne.class, "valueInjectedOne").getDemoInteger());
		assertEquals("Happy New Year!",
				dependencyInjector.getDependency(DemoInjectableOne.class, "valueInjectedOne").getDemoString());
	}

}
