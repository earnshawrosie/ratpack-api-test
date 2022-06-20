package ratpack.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import ratpack.config.RedisConfig;
import ratpack.error.UserDataException;
import ratpack.model.Balance;
import ratpack.model.User;
import ratpack.persistence.JedisUserRepository;
import redis.clients.jedis.JedisPool;

public class JedisUserRepositoryTest {
	
	
	private JedisUserRepository repo;
	
	@Test
	public void testRepository() throws UserDataException {
		RedisConfig config = new RedisConfig();
		config.setHost("localhost");
		config.setPort(6379);
		
		repo = new JedisUserRepository(jedisPool(config));
		
		final BigDecimal amount = new BigDecimal("5.45");
		final User user = new User(new Balance(amount, "GBP"));
		final String userId = user.getId();
		
		repo.save(userId, user);
		
		User repoUser = repo.getUser(userId);
		assertNotNull(repoUser);
		assertEquals(repoUser.getId(), user.getId());
		assertEquals(repoUser.getBalance().getBalance(), user.getBalance().getBalance());
	}
	
	private JedisPool jedisPool(RedisConfig config) {
	    return new JedisPool(config.getHost(), config.getPort());
	}
}
