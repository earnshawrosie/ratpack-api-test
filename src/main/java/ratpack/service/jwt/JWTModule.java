package ratpack.service.jwt;

import org.pac4j.jwt.config.encryption.EncryptionConfiguration;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.config.signature.SignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import ratpack.config.JWTConfig;

public class JWTModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton 
	public JwtGenerator jwtGenerator(SignatureConfiguration signatureConfiguration, EncryptionConfiguration encryptionConfiguration) {
		return new JwtGenerator(signatureConfiguration, encryptionConfiguration);
	}
	
	@Provides
	@Singleton 
	public JwtAuthenticator jwtAuthenticator(SignatureConfiguration signatureConfiguration, EncryptionConfiguration encryptionConfiguration) {
		return new JwtAuthenticator(signatureConfiguration, encryptionConfiguration);
	}

	@Provides
	@Singleton 
	public SignatureConfiguration signatureConfiguration(JWTConfig config) {
		return new SecretSignatureConfiguration(config.getSecret());
	}
	

	@Provides
	@Singleton 
	public EncryptionConfiguration encryptionConfiguration(JWTConfig config) {
		return new SecretEncryptionConfiguration(config.getSecret());
	}
}
