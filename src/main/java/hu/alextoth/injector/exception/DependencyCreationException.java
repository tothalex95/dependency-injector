package hu.alextoth.injector.exception;

/**
 * @author Alex Toth
 */
public class DependencyCreationException extends RuntimeException {

	private static final long serialVersionUID = -6699558150216608344L;

	/**
	 * @param message
	 */
	public DependencyCreationException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DependencyCreationException(String message, Throwable cause) {
		super(message, cause);
	}

}
