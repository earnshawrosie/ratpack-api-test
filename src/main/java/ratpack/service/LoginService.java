package ratpack.service;

import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.UserDataException;
import ratpack.model.Balance;
import ratpack.model.User;
import ratpack.util.BigDecimalGenerator;

public class LoginService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

	@Inject
	private UserService userService;

	public User login() throws UserDataException {
		User user = new User(new Balance(BigDecimalGenerator.generate().setScale(2, RoundingMode.HALF_UP), "GBP"));
		LOGGER.info("Creating USER with id " + user.getId());
		userService.saveUser(user.getId(), user);
		LOGGER.info("Created USER with id " + user.getId());
		return user;
	}

}
