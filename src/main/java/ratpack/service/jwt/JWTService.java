package ratpack.service.jwt;

import org.pac4j.core.profile.BasicUserProfile;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ratpack.model.User;

/**
 * Handles management of JWT
 *
 */
public class JWTService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(JWTService.class);
	
	private final JwtGenerator generator;
	
	private final JwtAuthenticator authenticator;
	
	@Inject
	public JWTService(JwtGenerator jwtGenerator, JwtAuthenticator jwtAuthenticator) {
		this.generator = jwtGenerator;
		this.authenticator = jwtAuthenticator;
	}

	public String generate(User user) {
		LOGGER.info("Generating jwt for user " + user);
		BasicUserProfile profile = new BasicUserProfile();
		profile.build(user.getId(), null);
		return generator.generate(profile);
	}

	public HeaderClient createHeaderClient() {
		return new HeaderClient("Authorization", "Bearer ", authenticator);
	}
}
