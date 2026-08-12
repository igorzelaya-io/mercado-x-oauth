package hn.shadowcore.mercadox.oauth.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.time.Duration;


@ConfigurationProperties(prefix = "security.jwt")
public record JwtSigningProperties(
        Resource privateKeyLocation,
        Duration expiration
) {

}