/**
 * 
 */
package hu.alextoth.injector.exception;

/**
 * @author Alex Toth
 *
 */
public class DependencyCreationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6699558150216608344L;

	/**
	 * 
	 */
	public DependencyCreationException() {
	}

	/**
	 * @param message
	 */
	public DependencyCreationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DependencyCreationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DependencyCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DependencyCreationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
