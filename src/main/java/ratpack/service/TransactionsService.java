package ratpack.service;

import java.util.List;

import org.pac4j.core.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.UserDataException;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.model.Transaction;
import ratpack.model.User;

/**
 * Handles Transaction queries.
 *
 */
public class TransactionsService {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Inject
	private UserService userService;

	public Promise<List<Transaction>> getTransactions(Context ctx) throws UserDataException {
		final UserProfile profile = ctx.get(UserProfile.class);
	
		LOGGER.info("Get transactions for user " + profile.getId());
		Promise<User> userPromise = userService.getUser(profile.getId());
		return userPromise.map(u -> u.getTransactions());
	}

}
