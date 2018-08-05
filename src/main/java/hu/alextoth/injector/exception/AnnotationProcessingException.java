package hu.alextoth.injector.exception;

/**
 * @author Alex Toth
 */
public class AnnotationProcessingException extends RuntimeException {

	private static final long serialVersionUID = -2047566256836444858L;

	/**
	 * @param message
	 */
	public AnnotationProcessingException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AnnotationProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

}
