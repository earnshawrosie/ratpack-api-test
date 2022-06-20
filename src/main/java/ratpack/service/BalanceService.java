package ratpack.service;

import org.pac4j.core.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.UserDataException;
import ratpack.exec.Operation;
import ratpack.exec.Promise;
import ratpack.model.Balance;
import ratpack.model.Transaction;
import ratpack.model.User;

public class BalanceService {

	private final static Logger LOGGER = LoggerFactory.getLogger(BalanceService.class);

	@Inject
	private UserService userService;

	public Promise<Balance> getBalance(final UserProfile profile) throws UserDataException {
		LOGGER.info("Balance request for user: " + profile.getId());
		Promise<User> userPromise = userService.getUser(profile.getId());
		return userPromise.map(u -> u.getBalance());
	}

	public void spend(final UserProfile profile, final Transaction tx) throws UserDataException {
		LOGGER.info("Spend request for user: " + profile.getId() + " with amount " + tx.getAmount() + " and currency "
				+ tx.getCurrency());
		
		Promise<User> userPromise = userService.getUser(profile.getId());
		userPromise.nextOp(user -> debitUser(user, tx))
		.then(user -> userService.saveUser(profile.getId(), user));
		
	}

	private Operation debitUser(User user, Transaction tx) throws UserDataException {
		return Operation.of(() -> {
			user.getBalance().debit(tx.getAmount());
			user.addTransaction(tx);
		});
	}

}
