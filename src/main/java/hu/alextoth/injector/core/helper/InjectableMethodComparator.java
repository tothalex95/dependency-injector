package hu.alextoth.injector.core.helper;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * Comparator class for sorting injectable methods based on their dependencies.
 * 
 * @author Alex Toth
 *
 */
public class InjectableMethodComparator implements Comparator<Method> {

	private final DependencyComparator dependencyComparator;

	public InjectableMethodComparator(DependencyComparator dependencyComparator) {
		this.dependencyComparator = dependencyComparator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Method o1, Method o2) {
		return dependencyComparator.compare(o1.getReturnType(), o2.getReturnType());
	}

}
