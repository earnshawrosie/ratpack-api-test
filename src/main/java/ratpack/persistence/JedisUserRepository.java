package ratpack.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.error.UserDataException;
import ratpack.model.User;
import ratpack.util.SerializeUtils;
import redis.clients.jedis.JedisPool;

public class JedisUserRepository implements UserRepository {

	private final static Logger LOGGER = LoggerFactory.getLogger(JedisUserRepository.class);

	private final JedisPool pool;

	@Inject
	public JedisUserRepository(JedisPool jedisPool) {
		this.pool = jedisPool;
	}

	@Override
	public User getUser(String key) throws UserDataException {
		LOGGER.info("Retrieving user " + key);

		byte[] userBytes = pool.getResource().get(key.getBytes());
		try {
			return (User) SerializeUtils.deSerialize(userBytes);
		} catch (Exception exc) {
			final String errMessage = "Error retrieving user details for " + key;
			LOGGER.error(errMessage, exc);
			throw new UserDataException(errMessage, exc);
		}
	}

	@Override
	public void save(String key, User user) throws UserDataException {
		LOGGER.info("Persisting user " + key);
		try {
			pool.getResource().set(key.getBytes(), SerializeUtils.serialize(user));
		} catch (Exception exc) {
			final String message = "Error persisting user " + key;
			LOGGER.error(message, exc);
			throw new UserDataException(message, exc);
		}
	}

}
