package ratpack.error;

public class BaseApplicationException extends Exception {

	public BaseApplicationException(String message, Exception e) {
		super(message, e);
	}

	private static final long serialVersionUID = 6150540288793073157L;

}
