package hu.alextoth.injector;

import hu.alextoth.injector.core.AnnotationProcessor;

public class DependencyInjector {

	private final AnnotationProcessor annotationProcessor;

	public DependencyInjector() {
		this.annotationProcessor = new AnnotationProcessor(null);
	}

}
