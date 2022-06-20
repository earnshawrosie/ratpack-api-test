package ratpack.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pac4j.core.profile.BasicUserProfile;

import ratpack.error.UserDataException;
import ratpack.exec.ExecResult;
import ratpack.exec.Execution;
import ratpack.exec.Promise;
import ratpack.func.Function;
import ratpack.model.Balance;
import ratpack.model.Transaction;
import ratpack.model.User;
import ratpack.test.exec.ExecHarness;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsServiceTest {

	@InjectMocks
	TransactionsService service;

	@Mock
	UserService userService;

	private ExecHarness harness = ExecHarness.harness();

	@Test
	public void getEmptyTransactionsTest() throws Exception {
		BigDecimal amount = new BigDecimal("5.45");
		when(userService.getUser(anyString())).thenReturn(Promise.value(new User(new Balance(amount, "GBP"))));

		String id = UUID.randomUUID().toString();
		BasicUserProfile profile = new BasicUserProfile();
		profile.build(id, null);

		final ExecResult<List<Transaction>> result = harness.yield(new Function<Execution, Promise<List<Transaction>>>() {
			@Override
			public Promise<List<Transaction>> apply(final Execution execution) throws UserDataException {
				return service.getTransactions(profile);
			}
		});

		assertTrue(result.getValue().isEmpty());

		verify(userService, times(1)).getUser(id);
	}
	
	@Test
	public void getTransactionsTest() throws Exception {
		BigDecimal amount = new BigDecimal("5.45");
		final BigDecimal toSpend = new BigDecimal("3.15");
		final User user = new User(new Balance(amount, "GBP"));
		user.addTransaction(new Transaction("2022-20-06T10:44:33Z", "desc", toSpend, "GBP"));
		when(userService.getUser(anyString())).thenReturn(Promise.value(user));

		String id = UUID.randomUUID().toString();
		BasicUserProfile profile = new BasicUserProfile();
		profile.build(id, null);

		final ExecResult<List<Transaction>> result = harness.yield(new Function<Execution, Promise<List<Transaction>>>() {
			@Override
			public Promise<List<Transaction>> apply(final Execution execution) throws UserDataException {
				return service.getTransactions(profile);
			}
		});

		assertEquals(1, result.getValue().size());

		verify(userService, times(1)).getUser(id);
	}

}
