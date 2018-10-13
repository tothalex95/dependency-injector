package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;
import hu.alextoth.injector.demo.DemoWrongAlias;
import hu.alextoth.injector.demo.DemoWrongAlias2;
import hu.alextoth.injector.demo.InjectablesWithoutConfiguration;
import hu.alextoth.injector.demo.InjectsWithoutComponent;

@ExtendWith(MockitoExtension.class)
public class DependencyAliasResolverTest {

	@Mock
	private AnnotationProcessorHelper annotationProcessorHelper;

	@InjectMocks
	private DependencyAliasResolver dependencyAliasResolver;

	@BeforeEach
	public void setUp() {
		Mockito.when(annotationProcessorHelper.isAliasAnnotation(Alias.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isAliasAnnotation(DemoAnnotation.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isAliasAnnotation(DemoWrongAlias.class)).thenReturn(true);

		Mockito.when(annotationProcessorHelper.isInjectableAnnotation(DemoAnnotation.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isInjectableAnnotation(DemoWrongAlias.class)).thenReturn(true);
		Mockito.when(annotationProcessorHelper.isInjectableAnnotation(DemoWrongAlias2.class)).thenReturn(true);
	}

	@AfterEach
	public void tearDown() {
		Mockito.reset(annotationProcessorHelper);
	}

	@Test
	public void testGetAliasOnField() throws NoSuchFieldException, SecurityException {
		Field field = AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne");

		assertEquals("alias2", dependencyAliasResolver.getAlias(field));
	}

	@Test
	public void testGetAliasOnParameter() {
		Parameter parameter = DemoInjectableTwo.class.getConstructors()[0].getParameters()[0];

		assertEquals("alias1", dependencyAliasResolver.getAlias(parameter));
	}

	@Test
	public void testGetAliasWithWrongAnnotation() throws NoSuchMethodException, SecurityException {
		Parameter parameter = InjectsWithoutComponent.class.getMethod("wrongAliasSetter", DemoInjectableThree.class)
				.getParameters()[0];

		assertThrows(IllegalArgumentException.class, () -> dependencyAliasResolver.getAlias(parameter));
	}

	@Test
	public void testGetAliases() throws NoSuchMethodException, SecurityException {
		Method method = ConfigClass.class.getMethod("getNamedDemoInjectableOne");

		String[] aliases = dependencyAliasResolver.getAliases(method);

		assertEquals(2, aliases.length);
		assertEquals("alias1", aliases[0]);
		assertEquals("alias2", aliases[1]);
	}

	@Test
	public void testGetAliasesWithWrongAnnotation() throws NoSuchMethodException, SecurityException {
		Method method = InjectablesWithoutConfiguration.class.getMethod("demoInjectableThree");

		assertThrows(IllegalArgumentException.class, () -> dependencyAliasResolver.getAliases(method));

		Method method2 = InjectablesWithoutConfiguration.class.getMethod("demoInjectableTwo");

		assertThrows(IllegalArgumentException.class, () -> dependencyAliasResolver.getAliases(method2));
	}

	@Test
	public void testGetAliasesWithoutAnnotation() throws NoSuchMethodException, SecurityException {
		Method method = DemoInjectableTwo.class.getMethod("getDemoInjectableOne");

		assertEquals(0, dependencyAliasResolver.getAliases(method).length);
	}

}
