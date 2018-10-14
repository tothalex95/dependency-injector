package hu.alextoth.injector.core.helper;

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

	public DependencySorter() {
		injectableMethodComparator = new InjectableMethodComparator(new DependencyComparator());
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

}
