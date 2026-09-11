package hn.alturaforge.mercadox.oauth.config;

import hn.alturaforge.mercadox.oauth.security.JwtSigner;
import hn.alturaforge.mercadox.oauth.security.JwtSigningProperties;
import hn.alturaforge.mercadox.oauth.security.PrivateKeyUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;

@Configuration
@EnableConfigurationProperties(JwtSigningProperties.class)
public class JwtSigningConfig {

    @Bean
    public RSAPrivateKey jwtPrivateKey(JwtSigningProperties properties) {
        return PrivateKeyUtils.readPrivateKey(properties.privateKeyLocation());
    }

    @Bean
    public JwtSigner jwtSigner(RSAPrivateKey privateKey, JwtSigningProperties properties) {
        return new JwtSigner(privateKey, properties.expiration(), properties.keyId());
    }

}