package hn.shadowcore.mercadoxoauth.util;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    private final JwtParser jwtParser;

    private final long jwtExpirationMs;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil(@Value("${security.jwt.secret}") String secret,
                   @Value("${security.jwt.expiration}") long jwtExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("orgId", user.getOrganization().getId().toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    public String getOrgIdFromToken(String token) {
        return getAllClaims(token).get("orgId", String.class);
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        }
        catch(JwtException ignored) {
            logger.error("JWT was invalid.");
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload();
    }

}
