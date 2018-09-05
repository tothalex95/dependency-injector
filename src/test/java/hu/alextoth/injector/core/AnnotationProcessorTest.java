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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reflections.Reflections;

import com.google.common.collect.Sets;

import hu.alextoth.injector.DependencyInjectorTest;
import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoAnnotation2;
import hu.alextoth.injector.demo.DemoInjectableFive;
import hu.alextoth.injector.demo.DemoInjectableFiveImpl;
import hu.alextoth.injector.demo.DemoInjectableFour;
import hu.alextoth.injector.demo.DemoInjectableFourImpl;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;
import hu.alextoth.injector.demo.DemoWrongAlias;
import hu.alextoth.injector.demo.DemoWrongAlias2;
import hu.alextoth.injector.demo.InjectablesWithoutConfiguration;
import hu.alextoth.injector.demo.InjectsWithoutComponent;

@ExtendWith(MockitoExtension.class)
@DemoAnnotation
public class AnnotationProcessorTest {

	@Mock
	private Reflections reflections;

	@Mock
	private DependencyAliasResolver dependencyAliasResolver;

	@Mock
	private DependencyHandler dependencyHandler;

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
		prepareReflections();
		prepareDependencyAliasResolver();
		prepareDependencyHandler();
	}

	private void prepareReflections() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		// Types annotated with Configuration
		Mockito.when(reflections.getTypesAnnotatedWith(Configuration.class))
				.thenReturn(Sets.newHashSet(DemoAnnotation.class, ConfigClass.class, AnnotationProcessorTest.class,
						DependencyInjectorTest.class));

		// Types annotated with Injectable
		Mockito.when(reflections.getTypesAnnotatedWith(Injectable.class))
				.thenReturn(Sets.newHashSet(DemoWrongAlias.class, DemoAnnotation2.class, DemoAnnotation.class,
						ConfigClass.class, DemoWrongAlias2.class, AnnotationProcessorTest.class,
						DependencyInjectorTest.class));

		// Types annotated with Component
		Mockito.when(reflections.getTypesAnnotatedWith(Component.class))
				.thenReturn(Sets.newHashSet(ConfigClass.class, DemoInjectableTwo.class, DemoAnnotation.class,
						DependencyInjectorTest.class, DemoInjectableThree.class, AnnotationProcessorTest.class));

		// Types annotated with Inject
		Mockito.when(reflections.getTypesAnnotatedWith(Inject.class)).thenReturn(Sets.newHashSet(ConfigClass.class,
				DemoAnnotation.class, DependencyInjectorTest.class, AnnotationProcessorTest.class));

		// Methods annotated with Injectable
		Mockito.when(reflections.getMethodsAnnotatedWith(Injectable.class))
				.thenReturn(
						Sets.newHashSet(InjectablesWithoutConfiguration.class.getDeclaredMethod("demoInjectableOne"),
								ConfigClass.class.getDeclaredMethod("getDemoInjectableOne")));

		// Methods annotated with Inject
		Mockito.when(reflections.getMethodsAnnotatedWith(Inject.class))
				.thenReturn(Sets.newHashSet(
						DemoInjectableThree.class.getDeclaredMethod("setDemoInjectableTwo", DemoInjectableTwo.class),
						InjectablesWithoutConfiguration.class.getDeclaredMethod("demoInjectableTwo"),
						InjectsWithoutComponent.class.getDeclaredMethod("setDemoInjectableThree",
								DemoInjectableThree.class),
						AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableOne",
								DemoInjectableOne.class)));

		// Methods annotated with DemoAnnotation
		Mockito.when(reflections.getMethodsAnnotatedWith(DemoAnnotation.class))
				.thenReturn(Sets.newHashSet(
						AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableThree",
								DemoInjectableThree.class),
						ConfigClass.class.getDeclaredMethod("getNamedDemoInjectableOne")));

		// Methods annotated with DemoAnnotation2
		Mockito.when(reflections.getMethodsAnnotatedWith(DemoAnnotation2.class))
				.thenReturn(Sets.newHashSet(ConfigClass.class.getDeclaredMethod("getNamedDemoInjectableOne2")));

		// Methods annotated with DemoWrongAlias
		Mockito.when(reflections.getMethodsAnnotatedWith(DemoWrongAlias.class))
				.thenReturn(Sets
						.newHashSet(InjectablesWithoutConfiguration.class.getDeclaredMethod("demoInjectableThree")));

		// Methods annotated with DemoWrongAlias2
		Mockito.when(reflections.getMethodsAnnotatedWith(DemoWrongAlias2.class))
				.thenReturn(
						Sets.newHashSet(InjectablesWithoutConfiguration.class.getDeclaredMethod("demoInjectableTwo")));

		// Fields annotated with Inject
		Mockito.when(reflections.getFieldsAnnotatedWith(Inject.class))
				.thenReturn(Sets.newHashSet(InjectsWithoutComponent.class.getDeclaredField("demoInjectableOne"),
						DependencyInjectorTest.class.getDeclaredField("demoInjectableOne"),
						AnnotationProcessorTest.class.getDeclaredField("demoInjectableFour"),
						DependencyInjectorTest.class.getDeclaredField("demoInjectableThree"),
						AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne"),
						AnnotationProcessorTest.class.getDeclaredField("demoInjectableTwo"),
						DependencyInjectorTest.class.getDeclaredField("demoInjectableTwo")));

		// Fields annotated with DemoAnnotation
		Mockito.when(reflections.getFieldsAnnotatedWith(DemoAnnotation.class))
				.thenReturn(Sets.newHashSet(AnnotationProcessorTest.class.getDeclaredField("demoInjectableFive")));

		// Constructors annotated with Inject
		Mockito.when(reflections.getConstructorsAnnotatedWith(Inject.class)).thenReturn(
				Sets.newHashSet(InjectsWithoutComponent.class.getDeclaredConstructor(DemoInjectableTwo.class)));

		// Constructors annotated with DemoAnnotation
		Mockito.when(reflections.getConstructorsAnnotatedWith(DemoAnnotation.class)).thenReturn(
				Sets.newHashSet(DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class)));
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
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableOne.class, "alias1")).thenReturn(demoInjectableOne12);
		Mockito.when(dependencyHandler.getInstanceOf(DemoInjectableOne.class, "alias2")).thenReturn(demoInjectableOne12);
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
		Mockito.reset(reflections, dependencyAliasResolver, dependencyHandler);
	}

	@Test
	public void testProcessAnnotations() {
		AnnotationProcessor annotationProcessor = new AnnotationProcessor(reflections, dependencyHandler,
				dependencyAliasResolver);
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
