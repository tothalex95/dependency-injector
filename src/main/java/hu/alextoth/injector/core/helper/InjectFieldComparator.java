package hu.alextoth.injector.core.helper;

import java.lang.reflect.Field;
import java.util.Comparator;

import hu.alextoth.injector.annotation.Inject;

/**
 * Comparator class for sorting fields annotated with {@link Inject} based on
 * the declaring class' dependencies.
 * 
 * @author Alex Toth
 */
public class InjectFieldComparator implements Comparator<Field> {

	private final DependencyComparator dependencyComparator;

	public InjectFieldComparator(DependencyComparator dependencyComparator) {
		this.dependencyComparator = dependencyComparator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Field o1, Field o2) {
		return dependencyComparator.compare(o1.getDeclaringClass(), o2.getDeclaringClass());
	}

}
