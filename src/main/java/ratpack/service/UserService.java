package ratpack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.UserDataException;
import ratpack.exec.Promise;
import ratpack.model.User;
import ratpack.persistence.JedisUserRepository;

/**
 * Handles user operations.
 *
 */
public class UserService {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Inject
	private JedisUserRepository userRepository;
	
	public Promise<User> getUser(String id) throws UserDataException {
		LOGGER.info("Searching for user " + id);
		return  Promise.value(userRepository.getUser(id));
	}

	public void saveUser(String id, User user) throws UserDataException {
		LOGGER.info("Saving user " + id);
		userRepository.save(id, user);
	}
}
