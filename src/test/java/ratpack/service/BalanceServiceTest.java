package ratpack.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
public class BalanceServiceTest {

	@InjectMocks
	BalanceService service;

	@Mock
	UserService userService;

	private ExecHarness harness = ExecHarness.harness();

	@Test
	public void getBalanceTest() throws Exception {
		BigDecimal amount = new BigDecimal("5.45");
		when(userService.getUser(anyString())).thenReturn(Promise.value(new User(new Balance(amount, "GBP"))));

		String id = UUID.randomUUID().toString();
		BasicUserProfile profile = new BasicUserProfile();
		profile.build(id, null);

		final ExecResult<Balance> result = harness.yield(new Function<Execution, Promise<Balance>>() {
			@Override
			public Promise<Balance> apply(final Execution execution) throws UserDataException {
				return service.getBalance(profile);
			}
		});

		assertEquals(result.getValue().getBalance(), amount);

		verify(userService, times(1)).getUser(id);
	}
	
	@Test
	public void spendTest() throws Exception {
		final BigDecimal amount = new BigDecimal("5.45");
		final User user = new User(new Balance(amount, "GBP"));
		
		when(userService.getUser(anyString())).thenReturn(Promise.value(user));

		String id = UUID.randomUUID().toString();
		BasicUserProfile profile = new BasicUserProfile();
		profile.build(id, null);
		
		final BigDecimal toSpend = new BigDecimal("1.73");
		Transaction tx = new Transaction("2022-20-06T10:44:33Z", "desc", toSpend, "GBP");
	
		harness.yield(new Function<Execution, Promise<String>>() {
			@Override
			public Promise<String> apply(final Execution execution) throws UserDataException {
				service.spend(profile, tx);
				return Promise.value("OK");
			}
		});

		verify(userService, times(1)).getUser(id);
		verify(userService, times(1)).saveUser(id, user);
	}

}
