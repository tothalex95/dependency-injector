package hu.alextoth.injector;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.core.AnnotationProcessor;
import hu.alextoth.injector.core.DependencyHandler;

/**
 * Main class of the dependency injection framework.
 * 
 * @author Alex Toth
 */
public class DependencyInjector {

	private final AnnotationProcessor annotationProcessor;
	private final DependencyHandler dependencyHandler;

	public DependencyInjector() {
		dependencyHandler = new DependencyHandler();
		annotationProcessor = new AnnotationProcessor(dependencyHandler);
	}

	/**
	 * Starts the processing of {@link Configuration}, {@link Injectable},
	 * {@link Component} and {@link Inject} annotations.
	 */
	public void injectDependencies() {
		annotationProcessor.processAnnotations();
	}

	/**
	 * Returns, or creates and registers if necessary, a registered instance of a
	 * given class.
	 * 
	 * @param clazz Class of which an instance must be returned.
	 * @return A registered instance of the given class.
	 */
	public <T> T getInstanceOf(Class<T> clazz) {
		return dependencyHandler.getInstanceOf(clazz);
	}

}
