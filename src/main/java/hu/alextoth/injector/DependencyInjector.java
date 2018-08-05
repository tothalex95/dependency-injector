package hu.alextoth.injector;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

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

	public DependencyInjector(String basePackage) {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(),
								new FieldAnnotationsScanner(), new MethodAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage(basePackage)));
		dependencyHandler = new DependencyHandler(reflections);
		annotationProcessor = new AnnotationProcessor(reflections, dependencyHandler);
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
