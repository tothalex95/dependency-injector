package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.demo.DemoAnnotation;
import hu.alextoth.injector.demo.DemoInjectableFive;
import hu.alextoth.injector.demo.DemoInjectableFour;
import hu.alextoth.injector.demo.DemoInjectableOne;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@DemoAnnotation
public class AnnotationProcessorTest {

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

	@Test
	public void testProcessAnnotations() {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(),
								new FieldAnnotationsScanner(), new MethodAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage("hu.alextoth.injector")));
		DependencyAliasResolver dependencyAliasResolver = new DependencyAliasResolver(reflections);
		AnnotationProcessor annotationProcessor = new AnnotationProcessor(reflections,
				new DependencyHandler(reflections, dependencyAliasResolver), dependencyAliasResolver);
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
