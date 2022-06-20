package ratpack.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.UserDataException;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;
import ratpack.model.Transaction;
import ratpack.service.BalanceService;

/*
 * Handler responsible for 'spend's
 */
public class SpendHandler implements Handler {

	private final static Logger LOGGER = LoggerFactory.getLogger(SpendHandler.class);

	@Inject
	private BalanceService balanceService;

	@Override
	public void handle(Context context) {
		LOGGER.info("Handling Spend Request");
		context.parse(Jackson.fromJson(Transaction.class)).then(t -> {
			try {
				balanceService.spend(context, t);
				LOGGER.info("Completed Spend Request");
				context.getResponse().status(200).send();
			} catch (UserDataException exc) {
				LOGGER.info("Exception handling Spend Request " + exc.getMessage());
				context.getResponse().status(500).send();
			}
		});

	}
}