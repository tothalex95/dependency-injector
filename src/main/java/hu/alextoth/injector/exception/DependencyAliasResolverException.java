package hu.alextoth.injector.exception;

/**
 * @author Alex Toth
 */
public class DependencyAliasResolverException extends RuntimeException {

	private static final long serialVersionUID = 1334799684014879557L;

	/**
	 * @param message
	 */
	public DependencyAliasResolverException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DependencyAliasResolverException(String message, Throwable cause) {
		super(message, cause);
	}

}
