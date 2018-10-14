package hu.alextoth.injector.core.helper;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Comparator class to determine whether a class is a dependency of another or
 * not.
 * 
 * @author Alex Toth
 */
public class DependencyComparator implements Comparator<Class<?>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		if (isDependencyOf(o1, o2)) {
			return 1;
		}
		if (isDependencyOf(o2, o1)) {
			return -1;
		}
		return 0;
	}

	/**
	 * Returns a boolean value indicating whether {@code dependency} is a dependency
	 * of {@code clazz} or not.
	 * 
	 * @param clazz
	 * @param dependency
	 * @return
	 */
	private boolean isDependencyOf(Class<?> clazz, Class<?> dependency) {
		return Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.getType().equals(dependency));
	}

}
