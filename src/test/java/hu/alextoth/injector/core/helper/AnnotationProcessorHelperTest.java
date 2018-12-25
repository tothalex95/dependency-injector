package hu.alextoth.injector.core.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
import hu.alextoth.injector.annotation.Value;
import hu.alextoth.injector.core.AnnotationProcessorTest;
import hu.alextoth.injector.core.MockitoExtension;
import hu.alextoth.injector.core.helper.AnnotationProcessorHelper;
import hu.alextoth.injector.demo.ConfigClass;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoAnnotation2;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;
import hu.alextoth.injector.demo.DemoWrongAlias;
import hu.alextoth.injector.demo.InjectablesWithoutConfiguration;
import hu.alextoth.injector.demo.InjectsWithoutComponent;

@ExtendWith(MockitoExtension.class)
public class AnnotationProcessorHelperTest {

	@Mock
	private Reflections reflections;

	@InjectMocks
	private AnnotationProcessorHelper annotationProcessorHelper;

	@BeforeEach
	public void setUp() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
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

		Mockito.when(reflections.getTypesAnnotatedWith(Value.class)).thenReturn(Sets.newHashSet(Value.class));

		Set<Method> methods = Sets.newHashSet();
		methods.addAll(Arrays.asList(AnnotationProcessorTest.class.getDeclaredMethods()));
		methods.addAll(Arrays.asList(ConfigClass.class.getDeclaredMethods()));

		Mockito.when(reflections.getMethodsAnnotatedWith(Injectable.class))
				.thenReturn(methods.stream().filter(method -> !method.isSynthetic()).collect(Collectors.toSet()));

		Mockito.when(reflections.getMethodsAnnotatedWith(DemoWrongAlias.class))
				.thenReturn(Sets.newHashSet(InjectablesWithoutConfiguration.class.getDeclaredMethods()));

		Mockito.when(reflections.getConstructorsAnnotatedWith(Inject.class)).thenReturn(
				Sets.newHashSet(InjectsWithoutComponent.class.getDeclaredConstructor(DemoInjectableTwo.class)));

		Mockito.when(reflections.getConstructorsAnnotatedWith(DemoAnnotation.class))
				.thenReturn(Sets.newHashSet(DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class)));

		Mockito.when(reflections.getFieldsAnnotatedWith(Inject.class))
				.thenReturn(Sets.newHashSet(InjectsWithoutComponent.class.getDeclaredField("demoInjectableOne"),
						AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne")));

		Mockito.when(reflections.getFieldsAnnotatedWith(DemoAnnotation.class))
				.thenReturn(Sets.newHashSet(AnnotationProcessorTest.class.getDeclaredField("demoInjectableFive")));

		Mockito.when(reflections.getFieldsAnnotatedWith(Value.class))
				.thenReturn(Sets.newHashSet(AnnotationProcessorTest.class.getDeclaredField("demoBooleanValue"),
						AnnotationProcessorTest.class.getDeclaredField("demoStringValue"),
						InjectsWithoutComponent.class.getDeclaredField("demoStringValue")));

		Mockito.when(reflections.getMethodsAnnotatedWith(Inject.class)).thenReturn(Sets.newHashSet(
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableOne", DemoInjectableOne.class),
				InjectsWithoutComponent.class.getDeclaredMethod("setDemoInjectableThree", DemoInjectableThree.class)));

		Mockito.when(reflections.getMethodsAnnotatedWith(DemoAnnotation.class)).thenReturn(Sets.newHashSet(
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableThree", DemoInjectableThree.class)));
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

		assertEquals(true, aliasAnnotations.containsAll(annotationProcessorHelper.getAliasAnnotations()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetValueAnnotations() {
		Set<Class<? extends Annotation>> valueAnnotations = Sets.newHashSet(Value.class);

		assertEquals(true, annotationProcessorHelper.getValueAnnotations().containsAll(valueAnnotations));

		assertEquals(true, valueAnnotations.containsAll(annotationProcessorHelper.getValueAnnotations()));
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
	public void testIsValueAnnotation() {
		assertEquals(true, annotationProcessorHelper.isValueAnnotation(Value.class));
		assertEquals(false, annotationProcessorHelper.isValueAnnotation(Test.class));
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
	public void testIsInjectConstructor() throws NoSuchMethodException, SecurityException {
		assertEquals(true, annotationProcessorHelper
				.isInjectConstructor(DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class)));
		assertEquals(false, annotationProcessorHelper
				.isInjectConstructor(InjectsWithoutComponent.class.getDeclaredConstructor(DemoInjectableTwo.class)));
	}

	@Test
	public void testIsInjectField() throws NoSuchFieldException, SecurityException {
		assertEquals(true, annotationProcessorHelper
				.isInjectField(AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne")));
		assertEquals(true, annotationProcessorHelper
				.isInjectField(AnnotationProcessorTest.class.getDeclaredField("demoInjectableFive")));
		assertEquals(false, annotationProcessorHelper
				.isInjectField(InjectsWithoutComponent.class.getDeclaredField("demoInjectableOne")));
	}

	@Test
	public void testIsInjectMethod() throws NoSuchMethodException, SecurityException {
		assertEquals(true, annotationProcessorHelper.isInjectMethod(
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableOne", DemoInjectableOne.class)));
		assertEquals(true, annotationProcessorHelper.isInjectMethod(
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableThree", DemoInjectableThree.class)));
		assertEquals(false, annotationProcessorHelper.isInjectMethod(
				InjectsWithoutComponent.class.getDeclaredMethod("setDemoInjectableThree", DemoInjectableThree.class)));
	}

	@Test
	public void testIsValueField() throws NoSuchFieldException, SecurityException {
		assertEquals(true,
				annotationProcessorHelper
						.isValueField(AnnotationProcessorTest.class.getDeclaredField("demoBooleanValue")));
		assertEquals(true, annotationProcessorHelper
				.isValueField(AnnotationProcessorTest.class.getDeclaredField("demoStringValue")));
		assertEquals(false, annotationProcessorHelper
				.isValueField(InjectsWithoutComponent.class.getDeclaredField("demoStringValue")));
		assertEquals(false, annotationProcessorHelper
				.isValueField(AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne")));
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

	@Test
	public void testGetInjectConstructors() throws NoSuchMethodException, SecurityException {
		Set<Constructor<?>> injectConstructors = Sets
				.newHashSet(DemoInjectableTwo.class.getDeclaredConstructor(DemoInjectableOne.class));

		assertEquals(true, annotationProcessorHelper.getInjectConstructors().containsAll(injectConstructors));
		assertEquals(true, injectConstructors.containsAll(annotationProcessorHelper.getInjectConstructors()));
	}

	@Test
	public void testGetInjectFields() throws NoSuchFieldException, SecurityException {
		Set<Field> injectFields = Sets.newHashSet(AnnotationProcessorTest.class.getDeclaredField("demoInjectableOne"),
				AnnotationProcessorTest.class.getDeclaredField("demoInjectableFive"));

		assertEquals(true, annotationProcessorHelper.getInjectFields().containsAll(injectFields));
		assertEquals(true, injectFields.containsAll(annotationProcessorHelper.getInjectFields()));
	}

	@Test
	public void testGetInjectMethods() throws NoSuchMethodException, SecurityException {
		Set<Method> injectMethods = Sets.newHashSet(
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableOne", DemoInjectableOne.class),
				AnnotationProcessorTest.class.getDeclaredMethod("setDemoInjectableThree", DemoInjectableThree.class));

		assertEquals(true, annotationProcessorHelper.getInjectMethods().containsAll(injectMethods));
		assertEquals(true, injectMethods.containsAll(annotationProcessorHelper.getInjectMethods()));
	}

	@Test
	public void testGetValueFields() throws NoSuchFieldException, SecurityException {
		Set<Field> valueFields = Sets.newHashSet(AnnotationProcessorTest.class.getDeclaredField("demoBooleanValue"),
				AnnotationProcessorTest.class.getDeclaredField("demoStringValue"));

		assertEquals(true, annotationProcessorHelper.getValueFields().containsAll(valueFields));
		assertEquals(true, valueFields.containsAll(annotationProcessorHelper.getValueFields()));
	}

}
