package hn.shadowcore.mercadox.oauth.security;

import hn.shadowcore.mercadox.library.entity.model.auth.Role;
import hn.shadowcore.mercadox.library.entity.model.auth.User;
import io.jsonwebtoken.Jwts;

import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtSigner {

    private static final String ISSUER = "mercadox-oauth";

    private final RSAPrivateKey privateKey;
    private final Duration expiration;

    public JwtSigner(
            RSAPrivateKey privateKey,
            Duration expiration
    ) {
        this.privateKey = privateKey;
        this.expiration = expiration;
    }

    public String generateToken(User user) {

        Instant now = Instant.now();

        List<String> roles = user.getUserRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(user.getEmail())
                .claim(
                        "orgId",
                        user.getOrganization().getId().toString()
                )
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }
}