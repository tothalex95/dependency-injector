package hu.alextoth.injector.core.helper;

import java.lang.reflect.Method;
import java.util.Comparator;

import hu.alextoth.injector.annotation.Inject;

/**
 * Comparator class for sorting methods annotated with {@link Inject} based on
 * the declaring class' dependencies.
 * 
 * @author Alex Toth
 */
public class InjectMethodComparator implements Comparator<Method> {

	private final DependencyComparator dependencyComparator;

	public InjectMethodComparator(DependencyComparator dependencyComparator) {
		this.dependencyComparator = dependencyComparator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Method o1, Method o2) {
		return dependencyComparator.compare(o1.getDeclaringClass(), o2.getDeclaringClass());
	}

}
