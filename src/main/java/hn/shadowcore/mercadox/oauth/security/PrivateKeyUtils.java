package hn.shadowcore.mercadox.oauth.security;

import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public final class PrivateKeyUtils {

    private PrivateKeyUtils() { }

    public static RSAPrivateKey readPrivateKey(Resource resource) {
        try {
            String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            String normalized = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(normalized);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));

        } catch (Exception e) {
            throw new IllegalStateException("Unable to load JWT private key", e);
        }
    }
}