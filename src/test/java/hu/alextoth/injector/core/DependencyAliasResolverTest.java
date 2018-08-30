package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;
import hu.alextoth.injector.demo.InjectablesWithoutConfiguration;
import hu.alextoth.injector.demo.InjectsWithoutComponent;
import hu.alextoth.injector.exception.DependencyAliasResolverException;

public class DependencyAliasResolverTest {

	private DependencyAliasResolver dependencyAliasResolver;

	@BeforeEach
	public void setUp() {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(),
								new FieldAnnotationsScanner(), new MethodAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage("hu.alextoth.injector")));
		dependencyAliasResolver = new DependencyAliasResolver(reflections);
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
		
		assertThrows(DependencyAliasResolverException.class, () -> dependencyAliasResolver.getAlias(parameter));
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

		assertThrows(DependencyAliasResolverException.class, () -> dependencyAliasResolver.getAliases(method));

		Method method2 = InjectablesWithoutConfiguration.class.getMethod("demoInjectableTwo");

		assertThrows(DependencyAliasResolverException.class, () -> dependencyAliasResolver.getAliases(method2));
	}

	@Test
	public void testGetAliasesWithoutAnnotation() throws NoSuchMethodException, SecurityException {
		Method method = DemoInjectableTwo.class.getMethod("getDemoInjectableOne");

		assertEquals(0, dependencyAliasResolver.getAliases(method).length);
	}

}
