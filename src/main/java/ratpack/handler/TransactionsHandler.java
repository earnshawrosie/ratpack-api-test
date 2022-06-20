package ratpack.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.UserDataException;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;
import ratpack.model.Transaction;
import ratpack.service.TransactionsService;

/*
 * Handler for Transaction info requests.
 */
public class TransactionsHandler implements Handler {

	private final static Logger LOGGER = LoggerFactory.getLogger(TransactionsHandler.class);

	@Inject
	private TransactionsService txService;

	@Override
	public void handle(Context context) {
		LOGGER.info("Handling Transactions Request");
		try {
			final Promise<List<Transaction>> promise = txService.getTransactions(context);
			promise.then(t -> context.render(Jackson.json(t)));
		} catch (UserDataException exc) {
			LOGGER.info("Exception handling Transactions Request " + exc.getMessage());
			context.getResponse().status(500).send();
		}
	}
}