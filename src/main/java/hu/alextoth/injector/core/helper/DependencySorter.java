package hu.alextoth.injector.core.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Class for sorting injectable methods and elements that must be injected by
 * their dependencies.
 * 
 * @author Alex Toth
 */
public class DependencySorter {

	private final InjectableMethodComparator injectableMethodComparator;
	private final InjectConstructorComparator injectConstructorComparator;

	public DependencySorter() {
		DependencyComparator dependencyComparator = new DependencyComparator();

		injectableMethodComparator = new InjectableMethodComparator(dependencyComparator);
		injectConstructorComparator = new InjectConstructorComparator(dependencyComparator);
	}

	/**
	 * Returns a sorted list of the given injectable methods.
	 * 
	 * @param injectableMethods A set of injectable methods that must be sorted.
	 * @return A sorted list of the given injectable methods.
	 */
	public List<Method> getSortedInjectableMethods(Set<Method> injectableMethods) {
		List<Method> sortedInjectableMethods = new ArrayList<>(injectableMethods);

		Collections.sort(sortedInjectableMethods, injectableMethodComparator);

		return sortedInjectableMethods;
	}

	/**
	 * Returns a sorted list of the given inject constructors.
	 * 
	 * @param injectConstructors A set of inject constructors that must be sorted.
	 * @return A sorted list of the given inject constructors.
	 */
	public List<Constructor<?>> getSortedInjectConstructors(Set<Constructor<?>> injectConstructors) {
		List<Constructor<?>> sortedInjectConstructors = new ArrayList<>(injectConstructors);

		Collections.sort(sortedInjectConstructors, injectConstructorComparator);

		return sortedInjectConstructors;
	}

}
