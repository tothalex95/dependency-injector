package hu.alextoth.injector.core.helper;

import java.lang.reflect.Constructor;
import java.util.Comparator;

import hu.alextoth.injector.annotation.Inject;

/**
 * Comparator class for sorting constructor annotated with {@link Inject} based
 * on the declaring class' dependencies.
 * 
 * @author Alex Toth
 */
public class InjectConstructorComparator implements Comparator<Constructor<?>> {

	private final DependencyComparator dependencyComparator;

	public InjectConstructorComparator(DependencyComparator dependencyComparator) {
		this.dependencyComparator = dependencyComparator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Constructor<?> o1, Constructor<?> o2) {
		return dependencyComparator.compare(o1.getDeclaringClass(), o2.getDeclaringClass());
	}

}
