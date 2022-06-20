package ratpack.persistence;

import ratpack.error.UserDataException;
import ratpack.model.User;

public interface UserRepository {

	User getUser(String key) throws UserDataException;

	void save(String key, User user) throws UserDataException;

}
