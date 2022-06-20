package ratpack.error;

public class UserDataException extends BaseApplicationException {

	public UserDataException(String message, Exception e) {
		super(message, e);
	}

	private static final long serialVersionUID = -2152111473297244433L;

}
