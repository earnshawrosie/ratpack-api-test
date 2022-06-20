package ratpack.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import ratpack.config.RedisConfig;
import redis.clients.jedis.JedisPool;

public class RedisModule extends AbstractModule {
	
	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	public JedisPool jedisPool(RedisConfig config) {
	    return new JedisPool(config.getHost(), config.getPort());
	}
	
}