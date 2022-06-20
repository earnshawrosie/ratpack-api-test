package ratpack;

import ratpack.api.ApiEndpoints;
import ratpack.api.ApiModule;
import ratpack.config.JWTConfig;
import ratpack.config.RedisConfig;
import ratpack.guice.Guice;
import ratpack.handling.RequestLogger;
import ratpack.persistence.RedisModule;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import ratpack.service.jwt.JWTModule;
import ratpack.session.SessionModule;
import ratpack.session.store.RedisSessionModule;

/**
 * Starts the application.
 */
public class Application {

	public static void main(String... args) throws Exception {
		RatpackServer.start(s -> s
				.serverConfig(c -> c.baseDir(BaseDir.find()).yaml("redis.yaml").yaml("jwt.yaml")
						.require("/redis", RedisConfig.class).require("/jwt", JWTConfig.class).build())
				.registry(
						Guice.registry(b -> b.module(ApiModule.class).module(RedisModule.class).module(JWTModule.class)
								.module(SessionModule.class).module(RedisSessionModule.class, config -> {
									config.setHost("localhost");
									config.setPort(6379);

								})))
				.handlers(c -> c.all(RequestLogger.ncsa()).insert(ApiEndpoints.class)
						.get(ctx -> ctx.render("Ratpack Technical Test"))));
	}
}
