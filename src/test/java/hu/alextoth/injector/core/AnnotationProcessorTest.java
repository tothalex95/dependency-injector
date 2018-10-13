package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.common.collect.Sets;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoInjectableFive;
import hu.alextoth.injector.demo.DemoInjectableFiveImpl;
import hu.alextoth.injector.demo.DemoInjectableFour;
import hu.alextoth.injector.demo.DemoInjectableFourImpl;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@ExtendWith(MockitoExtension.class)
@DemoAnnotation
public class AnnotationProcessorTest {

	@Mock
	private AnnotationProcessorHelper annotationProcessorHelper;

	@Mock
	private DependencyAliasResolver dependencyAliasResolver;

	@Mock
	private DependencyHandler dependencyHandler;

	@InjectMocks
	private AnnotationProcessor annotationProcessor;

	@Inject
	@Alias("alias2")
	private static DemoInjectableOne demoInjectableOne;

	private static DemoInjectableOne demoInjectableOne2;

	@Inject
	private static DemoInjectableTwo demoInjectableTwo;

	private static DemoInjectableThree demoInjectableThree;

	@Inject
	private static DemoInjectableFour demoInjectableFour;

	@DemoAnnotation
	private static DemoInjectableFive demoInjectableFive;

	@BeforeEach
	public void setUp() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		prepareAnnotationProcessorHelper();
		prepareDependencyAliasResolver();
		prepareDependencyHandler();
	}

	private void prepareAnnotationProcessorHelper()
			throws NoSuchFieldException, NoSuchMethodException, SecurityException {
		// Injectable methods
		Mockito.when(annotationProcessorHelper.getInjectableMethods())
				.thenReturn(Sets.newHashSet(ConfigClass.class.getDeclaredMethods()));

		// Configuration classes
		Mockito.when(annotationProcessorHelper.isConfigurationClass(ConfigClass.class)).thenReturn(true);

		// Inject constructors
		Mockito.when(annotationProcessorHelper.getInjectConstructors())
				.thenReturn(Sets.newHashSet(DemoInjectableTwo.class.getConstructor(DemoInjectableOne.class)));

		// Inject fields
		Mockito.when(annotationProcessorHelper.getInjectFields())
				.thenReturn(Sets.newHashSet(AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne"),
						AnnotationProcessorTest.class.getDeclaredField("demoInjectableTwo"),
						AnnotationProcessorTest.class.getDeclaredField("demoInjectableFour"),
						AnnotationProcessorTest.class.getDeclaredField("demoInjectableFive")));

		// Inject methods
		Mockito.when(annotationProcessorHelper.getInjectMethods()).thenReturn(Sets.newHashSet(
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableOne", DemoInjectableOne.class),
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableThree", DemoInjectableThree.class),
				DemoInjectableThree.class.getDeclaredMethod("setDemoInjectableTwo", DemoInjectableTwo.class)));

		// Component classes
		Mockito.when(annotationProcessorHelper.isComponentClass(AnnotationProcessorTest.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isComponentClass(DemoInjectableTwo.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isComponentClass(DemoInjectableThree.class)).thenReturn(true);
	}

	private void prepareDependencyAliasResolver()
			throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		// Default alias
		Mockito.when(dependencyAliasResolver.getAlias((Field) Mockito.notNull())).thenReturn(Alias.DEFAULT_ALIAS);
		Mockito.when(dependencyAliasResolver.getAlias((Parameter) Mockito.notNull())).thenReturn(Alias.DEFAULT_ALIAS);
		Mockito.when(dependencyAliasResolver.getAliases((Method) Mockito.notNull()))
				.thenReturn(new String[] { Alias.DEFAULT_ALIAS });

		// Aliases in AnnotationProcessorTest
		Mockito.when(
				dependencyAliasResolver.getAlias(AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne")))
				.thenReturn("alias2");

		Mockito.when(dependencyAliasResolver.getAlias(AnnotationProcessorTest.class
				.getDeclaredMethod("setDemoInjectableOne", DemoInjectableOne.class).getParameters()[0]))
				.thenReturn("alias1");

		// Aliases in ConfigClass
		Mockito.when(
				dependencyAliasResolver.getAliases(ConfigClass.class.getDeclaredMethod("getNamedDemoInjectableOne")))
				.thenReturn(new String[] { "alias1", "alias2" });

		Mockito.when(
				dependencyAliasResolver.getAliases(ConfigClass.class.getDeclaredMethod("getNamedDemoInjectableOne2")))
				.thenReturn(new String[] { "alias3" });

		// Aliases in DemoInjectableTwo
		Mockito.when(dependencyAliasResolver
				.getAlias(DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class).getParameters()[0]))
				.thenReturn("alias1");
	}

	private void prepareDependencyHandler() {
		ConfigClass configClass = new ConfigClass();
		Mockito.when(dependencyHandler.getInstanceOf(ConfigClass.class)).thenReturn(configClass);

		Mockito.when(dependencyHandler.getInstanceOf(AnnotationProcessorTest.class)).thenReturn(this);

		DemoInjectableOne demoInjectableOne12 = new DemoInjectableOne(2018, "namedDependency");
		DemoInjectableOne demoInjectableOne3 = new DemoInjectableOne(2018, "namedDependency2");
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableOne.class, "alias1"))
				.thenReturn(demoInjectableOne12);
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableOne.class, "alias2"))
				.thenReturn(demoInjectableOne12);
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableOne.class, "alias3")).thenReturn(demoInjectableOne3);

		DemoInjectableTwo demoInjectableTwo = new DemoInjectableTwo(demoInjectableOne12);
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableTwo.class)).thenReturn(demoInjectableTwo);
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableTwo.class, Alias.DEFAULT_ALIAS))
				.thenReturn(demoInjectableTwo);

		DemoInjectableThree demoInjectableThree = new DemoInjectableThree();
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableThree.class)).thenReturn(demoInjectableThree);
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableThree.class, Alias.DEFAULT_ALIAS))
				.thenReturn(demoInjectableThree);

		DemoInjectableFour demoInjectableFour = new DemoInjectableFourImpl();
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableFour.class, Alias.DEFAULT_ALIAS))
				.thenReturn(demoInjectableFour);

		DemoInjectableFive demoInjectableFive = new DemoInjectableFiveImpl();
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableFive.class, Alias.DEFAULT_ALIAS))
				.thenReturn(demoInjectableFive);
	}

	@AfterEach
	public void tearDown() {
		Mockito.reset(annotationProcessorHelper, dependencyAliasResolver, dependencyHandler);
	}

	@Test
	public void testProcessAnnotations() {
		annotationProcessor.processAnnotations();

		assertNotNull(demoInjectableOne);
		assertNotNull(demoInjectableOne2);
		assertNotNull(demoInjectableTwo);
		assertNotNull(demoInjectableThree);
		assertNotNull(demoInjectableFour);
		assertNotNull(demoInjectableFive);

		assertEquals(demoInjectableOne, demoInjectableTwo.getDemoInjectableOne());
		assertEquals(demoInjectableOne, demoInjectableOne2);

		assertNotNull(demoInjectableTwo.getDemoInjectableOne());
		assertEquals(Integer.valueOf(2018), demoInjectableTwo.getDemoInjectableOne().getDemoInteger());
		assertEquals("namedDependency", demoInjectableTwo.getDemoInjectableOne().getDemoString());

		assertNotNull(demoInjectableThree.getDemoInjectableTwo());
		assertNotNull(demoInjectableThree.getDemoInjectableTwo().getDemoInjectableOne());

		assertEquals(demoInjectableTwo, demoInjectableThree.getDemoInjectableTwo());
		assertEquals(demoInjectableTwo.getDemoInjectableOne(),
				demoInjectableThree.getDemoInjectableTwo().getDemoInjectableOne());
	}

	@Inject
	public void setDemoInjectableOne(@Alias("alias1") DemoInjectableOne demoInjectableOne) {
		AnnotationProcessorTest.demoInjectableOne2 = demoInjectableOne;
	}

	@DemoAnnotation
	public void setDemoInjectableThree(DemoInjectableThree demoInjectableThree) {
		AnnotationProcessorTest.demoInjectableThree = demoInjectableThree;
	}

}
