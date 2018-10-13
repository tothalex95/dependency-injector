package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reflections.Reflections;

import com.google.common.collect.Sets;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoAnnotation2;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;
import hu.alextoth.injector.demo.DemoWrongAlias;
import hu.alextoth.injector.demo.InjectablesWithoutConfiguration;

@ExtendWith(MockitoExtension.class)
public class AnnotationProcessorHelperTest {

	@Mock
	private Reflections reflections;

	@InjectMocks
	private AnnotationProcessorHelper annotationProcessorHelper;

	@BeforeEach
	public void setUp() {
		Mockito.when(reflections.getTypesAnnotatedWith(Component.class))
				.thenReturn(Sets.newHashSet(DemoAnnotation.class, Component.class, DemoInjectableTwo.class));

		Mockito.when(reflections.getTypesAnnotatedWith(Configuration.class))
				.thenReturn(Sets.newHashSet(ConfigClass.class, DemoAnnotation.class, Configuration.class));

		Mockito.when(reflections.getTypesAnnotatedWith(Injectable.class)).thenReturn(
				Sets.newHashSet(DemoAnnotation.class, Injectable.class, DemoAnnotation2.class, DemoWrongAlias.class));

		Mockito.when(reflections.getTypesAnnotatedWith(Inject.class))
				.thenReturn(Sets.newHashSet(DemoAnnotation.class, Inject.class));

		Mockito.when(reflections.getTypesAnnotatedWith(Alias.class))
				.thenReturn(Sets.newHashSet(DemoAnnotation.class, Alias.class));

		Set<Method> methods = Sets.newHashSet();
		methods.addAll(Arrays.asList(AnnotationProcessorTest.class.getDeclaredMethods()));
		methods.addAll(Arrays.asList(ConfigClass.class.getDeclaredMethods()));

		Mockito.when(reflections.getMethodsAnnotatedWith(Injectable.class))
				.thenReturn(methods.stream().filter(method -> !method.isSynthetic()).collect(Collectors.toSet()));

		Mockito.when(reflections.getMethodsAnnotatedWith(DemoWrongAlias.class))
				.thenReturn(Sets.newHashSet(InjectablesWithoutConfiguration.class.getDeclaredMethods()));
	}

	@AfterEach
	public void tearDown() {
		Mockito.reset(reflections);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetComponentAnnotations() {
		Set<Class<? extends Annotation>> componentAnnotations = Sets.newHashSet(Component.class, DemoAnnotation.class);

		assertEquals(true, annotationProcessorHelper.getComponentAnnotations().containsAll(componentAnnotations));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetConfigurationAnnotations() {
		Set<Class<? extends Annotation>> configurationAnnotations = Sets.newHashSet(Configuration.class,
				DemoAnnotation.class);

		assertEquals(true,
				annotationProcessorHelper.getConfigurationAnnotations().containsAll(configurationAnnotations));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetInjectableAnnotations() {
		Set<Class<? extends Annotation>> injectableAnnotations = Sets.newHashSet(Injectable.class,
				DemoAnnotation.class);

		assertEquals(true, annotationProcessorHelper.getInjectableAnnotations().containsAll(injectableAnnotations));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetInjectAnnotations() {
		Set<Class<? extends Annotation>> injectAnnotations = Sets.newHashSet(Inject.class, DemoAnnotation.class);

		assertEquals(true, annotationProcessorHelper.getInjectAnnotations().containsAll(injectAnnotations));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAliasAnnotations() {
		Set<Class<? extends Annotation>> aliasAnnotations = Sets.newHashSet(Alias.class, DemoAnnotation.class);

		assertEquals(true, annotationProcessorHelper.getAliasAnnotations().containsAll(aliasAnnotations));

		assertEquals(true, annotationProcessorHelper.getAliasAnnotations().containsAll(aliasAnnotations));
	}

	@Test
	public void testIsComponentAnnotation() {
		assertEquals(true, annotationProcessorHelper.isComponentAnnotation(Component.class));
		assertEquals(true, annotationProcessorHelper.isComponentAnnotation(DemoAnnotation.class));
		assertEquals(false, annotationProcessorHelper.isComponentAnnotation(Test.class));
	}

	@Test
	public void testIsConfigurationAnnotation() {
		assertEquals(true, annotationProcessorHelper.isConfigurationAnnotation(Configuration.class));
		assertEquals(true, annotationProcessorHelper.isConfigurationAnnotation(DemoAnnotation.class));
		assertEquals(false, annotationProcessorHelper.isConfigurationAnnotation(Test.class));
	}

	@Test
	public void testIsInjectableAnnotation() {
		assertEquals(true, annotationProcessorHelper.isInjectableAnnotation(Injectable.class));
		assertEquals(true, annotationProcessorHelper.isInjectableAnnotation(DemoAnnotation.class));
		assertEquals(false, annotationProcessorHelper.isInjectableAnnotation(Test.class));
	}

	@Test
	public void testIsInjectAnnotation() {
		assertEquals(true, annotationProcessorHelper.isInjectAnnotation(Inject.class));
		assertEquals(true, annotationProcessorHelper.isInjectAnnotation(DemoAnnotation.class));
		assertEquals(false, annotationProcessorHelper.isInjectAnnotation(Test.class));
	}

	@Test
	public void testIsAliasAnnotation() {
		assertEquals(true, annotationProcessorHelper.isAliasAnnotation(Alias.class));
		assertEquals(true, annotationProcessorHelper.isAliasAnnotation(DemoAnnotation.class));
		assertEquals(false, annotationProcessorHelper.isAliasAnnotation(Test.class));
	}

	@Test
	public void testIsComponentClass() {
		assertEquals(true, annotationProcessorHelper.isComponentClass(DemoInjectableTwo.class));
		assertEquals(false, annotationProcessorHelper.isComponentClass(AnnotationProcessorHelperTest.class));
	}

	@Test
	public void testIsConfigurationClass() {
		assertEquals(true, annotationProcessorHelper.isConfigurationClass(ConfigClass.class));
		assertEquals(false, annotationProcessorHelper.isConfigurationClass(AnnotationProcessorHelperTest.class));
	}

	@Test
	public void testIsInjectableMethod() throws NoSuchMethodException, SecurityException {
		assertEquals(true,
				annotationProcessorHelper.isInjectableMethod(ConfigClass.class.getMethod("getDemoInjectableOne")));
		assertEquals(true,
				annotationProcessorHelper.isInjectableMethod(ConfigClass.class.getMethod("getNamedDemoInjectableOne")));
		assertEquals(false, annotationProcessorHelper
				.isInjectableMethod(InjectablesWithoutConfiguration.class.getMethod("demoInjectableOne")));
		assertEquals(false, annotationProcessorHelper
				.isInjectableMethod(AnnotationProcessorHelperTest.class.getMethod("testIsInjectableMethod")));
		assertEquals(false, annotationProcessorHelper.isInjectableMethod(
				AnnotationProcessorTest.class.getMethod("setDemoInjectableThree", DemoInjectableThree.class)));
	}

	@Test
	public void testGetComponentClasses() {
		Set<Class<?>> componentClasses = Sets.newHashSet(DemoInjectableTwo.class);

		assertEquals(true, annotationProcessorHelper.getComponentClasses().containsAll(componentClasses));
	}

	@Test
	public void testGetConfigurationClasses() {
		Set<Class<?>> configurationClasses = Sets.newHashSet(ConfigClass.class);

		assertEquals(true, annotationProcessorHelper.getConfigurationClasses().containsAll(configurationClasses));
	}

	@Test
	public void testGetInjectableMethods() {
		Set<Method> injectableMethods = Arrays.stream(ConfigClass.class.getDeclaredMethods())
				.filter(method -> !method.isSynthetic()).collect(Collectors.toSet());

		assertEquals(true, annotationProcessorHelper.getInjectableMethods().containsAll(injectableMethods));
		assertEquals(true, injectableMethods.containsAll(annotationProcessorHelper.getInjectableMethods()));
	}

}
