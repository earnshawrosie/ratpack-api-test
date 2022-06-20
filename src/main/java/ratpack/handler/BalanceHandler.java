package ratpack.handler;

import org.pac4j.core.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.BaseApplicationException;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;
import ratpack.model.Balance;
import ratpack.service.BalanceService;

/**
 * Handler responsible for retrieving balance details.
 */
public class BalanceHandler implements Handler {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(BalanceHandler.class);
	
	@Inject 
	private BalanceService balanceService;

	@Override
	public void handle(Context context) {
		LOGGER.info("Handling Balance Request");
		try {
			final Promise<Balance> balancePromise = balanceService.getBalance(context.get(UserProfile.class));
			balancePromise.then(b -> context.render(Jackson.json(b)));
		} catch (BaseApplicationException exc) {
			LOGGER.info("Exception handling Balance Request " + exc.getMessage());
			context.getResponse().status(500).send();
		}
	}
}