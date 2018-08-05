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

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.demo.DemoInjectableFive;
import hu.alextoth.injector.demo.DemoInjectableFour;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@Component
public class AnnotationProcessorTest {

	@Inject
	private static DemoInjectableTwo demoInjectableTwo;

	private static DemoInjectableThree demoInjectableThree;

	@Inject
	private static DemoInjectableFour demoInjectableFour;

	@Inject
	private static DemoInjectableFive demoInjectableFive;

	@Test
	void testProcessAnnotations() {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(),
								new FieldAnnotationsScanner(), new MethodAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage("hu.alextoth.injector")));
		AnnotationProcessor annotationProcessor = new AnnotationProcessor(reflections,
				new DependencyHandler(reflections));
		annotationProcessor.processAnnotations();

		assertNotNull(demoInjectableTwo);
		assertNotNull(demoInjectableThree);
		assertNotNull(demoInjectableFour);
		assertNotNull(demoInjectableFive);

		assertNotNull(demoInjectableTwo.getDemoInjectableOne());
		assertEquals(demoInjectableTwo.getDemoInjectableOne().getDemoInteger(), Integer.valueOf(2018));
		assertEquals(demoInjectableTwo.getDemoInjectableOne().getDemoString(), "Alex Toth");

		assertNotNull(demoInjectableThree.getDemoInjectableTwo());
		assertNotNull(demoInjectableThree.getDemoInjectableTwo().getDemoInjectableOne());

		assertEquals(demoInjectableTwo, demoInjectableThree.getDemoInjectableTwo());
		assertEquals(demoInjectableTwo.getDemoInjectableOne(),
				demoInjectableThree.getDemoInjectableTwo().getDemoInjectableOne());
	}

	@Inject
	public void setMethodLevelInjection(DemoInjectableThree demoInjectableThree) {
		AnnotationProcessorTest.demoInjectableThree = demoInjectableThree;
	}

}
