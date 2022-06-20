package ratpack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import io.netty.handler.codec.http.HttpHeaderNames;
import ratpack.handler.BalanceHandler;
import ratpack.handler.LoginHandler;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.model.Balance;
import ratpack.model.Transaction;
import ratpack.test.CloseableApplicationUnderTest;
import ratpack.test.MainClassApplicationUnderTest;
import ratpack.test.http.TestHttpClient;

public class RatpackTest {

	@Inject
	LoginHandler handler;

	@Inject
	BalanceHandler bHandler;

	private final CloseableApplicationUnderTest aut = new MainClassApplicationUnderTest(Application.class);
	private final TestHttpClient httpClient = aut.getHttpClient();

	private static final BigDecimal toSpend = new BigDecimal("4.2589");

	@After
	public void tearDown() throws Exception {
		aut.close();
	}

	@Test
	public void redirectsToIndexHtml() {
		final ReceivedResponse response = httpClient.get();
		assertEquals(200, response.getStatusCode());
		assertEquals("Ratpack Technical Test", response.getBody().getText());
	}

	@Test
	public void login() {
		final ReceivedResponse response = httpClient.post("/login");
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void balanceRequiresAuth() {
		ReceivedResponse response = httpClient.get("/balance");
		assertEquals(401, response.getStatusCode());
	}

	@Test
	public void spendRequiresAuth() {
		ReceivedResponse response = httpClient.get("/spend");
		assertEquals(401, response.getStatusCode());
	}

	@Test
	public void transactionsRequiresAuth() {
		ReceivedResponse response = httpClient.get("/balance");
		assertEquals(401, response.getStatusCode());
	}

	@Test
	public void testGetBalance() throws JsonMappingException, JsonProcessingException {
		final ReceivedResponse loginResponse = httpClient.post("/login");
		assertEquals(200, loginResponse.getStatusCode());
		assertNotNull(loginResponse.getBody().getText());

		String token = getToken();

		getBalance(token);
	}

	@Test
	public void testSpend() throws JsonMappingException, JsonProcessingException {
		String token = getToken();
		Balance balance = getBalance(token);

		spend(token);

		Balance balanceAfterSpend = getBalance(token);

		assertEquals(balanceAfterSpend.getBalance(),
				balance.getBalance().subtract(toSpend).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	public void testGetTransactions() throws JsonMappingException, JsonProcessingException {
		String token = getToken();

		// Initially tx list should be empty
		List<Transaction> txs = getTransactions(token);
		assertTrue(txs.isEmpty());

		// Perform a spend transaction
		spend(token);

		// Should have 1 transaction
		txs = getTransactions(token);
		assertEquals(1, txs.size());

		Transaction tx = txs.get(0);
		assertEquals(toSpend, tx.getAmount());

		// Perform a second spend transaction
		spend(token);

		// Should have 2 transactions
		txs = getTransactions(token);
		assertEquals(2, txs.size());
	}

	private String getToken() {
		final ReceivedResponse loginResponse = httpClient.post("/login");
		assertEquals(200, loginResponse.getStatusCode());
		assertNotNull(loginResponse.getBody().getText());

		return "Bearer " + loginResponse.getBody().getText().replaceAll("\"", "");
	}

	private Balance getBalance(final String token) throws JsonMappingException, JsonProcessingException {
		ReceivedResponse balanceResponse = httpClient.requestSpec(
				r -> r.getHeaders().add("Content-Type", "application/json").add(HttpHeaderNames.AUTHORIZATION, token))
				.get("/balance");

		assertEquals(200, balanceResponse.getStatusCode());

		assertNotNull(balanceResponse.getBody().getText());

		ObjectMapper mapper = new ObjectMapper();
		Balance balance = mapper.readValue(balanceResponse.getBody().getText(), Balance.class);
		assertNotNull(balance.getBalance());
		assertNotNull(balance.getCurrency());

		return balance;
	}

	private List<Transaction> getTransactions(final String token) throws JsonMappingException, JsonProcessingException {
		ReceivedResponse txResponse = httpClient.requestSpec(
				r -> r.getHeaders().add("Content-Type", "application/json").add(HttpHeaderNames.AUTHORIZATION, token))
				.get("/transactions");

		assertEquals(200, txResponse.getStatusCode());

		assertNotNull(txResponse.getBody().getText());

		ObjectMapper mapper = new ObjectMapper();
		return Arrays.asList(mapper.readValue(txResponse.getBody().getText(), Transaction[].class));
	}

	private void spend(final String token) {
		httpClient.requestSpec(r -> initSpendRequest(r, token));

		final ReceivedResponse spendResponse = httpClient.post("/spend");

		assertEquals(200, spendResponse.getStatusCode());

	}

	private RequestSpec initSpendRequest(RequestSpec r, String token) throws JsonProcessingException {
		Transaction tx = new Transaction("2022-20-06T10:44:33Z", "desc", toSpend, "GBP");
		ObjectMapper mapper = new ObjectMapper();
		String txJson = mapper.writeValueAsString(tx);

		r.getHeaders().add("Content-Type", "application/json").add(HttpHeaderNames.AUTHORIZATION, token);
		r.getBody().text(txJson);
		return r;
	}
}
