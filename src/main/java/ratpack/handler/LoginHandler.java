package ratpack.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.BaseApplicationException;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;
import ratpack.model.User;
import ratpack.service.LoginService;
import ratpack.service.jwt.JWTService;

/**
 * Handler responsible for Login. Generates a new auth token and creates a new
 * user with preset balance and currency.
 */
public class LoginHandler implements Handler {

	private final static Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

	@Inject
	private JWTService jwtService;

	@Inject
	private LoginService loginService;

	@Override
	public void handle(Context context) {
		LOGGER.info("Handling Login Request");
		try {
			final User user = loginService.login();
			final String jwt = jwtService.generate(user);
			LOGGER.info("Logged in user: " + user.getId());
			context.render(Jackson.json(jwt));
		} catch (BaseApplicationException exc) {
			LOGGER.info("Exception handling Login Request " + exc.getMessage());
			context.getResponse().status(500).send();
		}
	}
}