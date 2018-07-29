/**
 * 
 */
package hu.alextoth.injector.exception;

/**
 * @author Alex Toth
 *
 */
public class AnnotationProcessingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2047566256836444858L;

	/**
	 * 
	 */
	public AnnotationProcessingException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public AnnotationProcessingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public AnnotationProcessingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AnnotationProcessingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AnnotationProcessingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
