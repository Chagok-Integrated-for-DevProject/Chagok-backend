package site.chagok.server.security.config;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public RsaJsonWebKey initRsaJsonWebKey() throws JoseException {

        return RsaJwkGenerator.generateJwk(2048);
    }
}
