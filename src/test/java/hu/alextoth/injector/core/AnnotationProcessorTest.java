package hu.alextoth.injector.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.demo.DemoInjectableThree;
import hu.alextoth.injector.demo.DemoInjectableTwo;

@Component
public class AnnotationProcessorTest {

	@Inject
	private static DemoInjectableTwo demoInjectableTwo;

	private static DemoInjectableThree demoInjectableThree;

	@Test
	void testProcessAnnotations() {
		AnnotationProcessor annotationProcessor = new AnnotationProcessor(new DependencyHandler());
		annotationProcessor.processAnnotations();

		assertNotNull(demoInjectableTwo);
		assertNotNull(demoInjectableThree);

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
