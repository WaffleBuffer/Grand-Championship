package gameExceptions;

public class GameException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1642409915850351094L;

	public enum ExceptionType {
		OBJECT_WEIGHT, REQUIRED_TRAIT
	}
	
	private final ExceptionType exceptionType;
	
	public GameException(final ExceptionType exceptionType) {
		super();
		this.exceptionType = exceptionType;
	}

	public GameException(
			final String message, 
			final Throwable cause, 
			final boolean enableSuppression, 
			final boolean writableStackTrace,
			final ExceptionType exceptionType) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.exceptionType = exceptionType;
	}

	public GameException(final String message, final Throwable cause, final ExceptionType exceptionType) {
		super(message, cause);
		this.exceptionType = exceptionType;
	}

	public GameException(String message, final ExceptionType exceptionType) {
		super(message);
		this.exceptionType = exceptionType;
	}

	@Override
	public String toString() {
		return exceptionType + System.lineSeparator() + super.toString();
	}
}
