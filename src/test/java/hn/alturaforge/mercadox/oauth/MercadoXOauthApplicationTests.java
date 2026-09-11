package hn.alturaforge.mercadox.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@SpringBootTest
@ActiveProfiles("test")
class MercadoXOauthApplicationTests {

    private static final JwtKeyFiles JWT_KEY_FILES = createJwtKeyFiles();

    @DynamicPropertySource
    static void jwtKeyLocations(DynamicPropertyRegistry registry) {
        registry.add("security.jwt.private-key-location",
                () -> JWT_KEY_FILES.privateKey().toUri().toString());
        registry.add("security.jwt.public-key-location",
                () -> JWT_KEY_FILES.publicKey().toUri().toString());
    }

    @Test
    void contextLoads() {
    }

    private static JwtKeyFiles createJwtKeyFiles() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();

            Path privateKey = Files.createTempFile("mercadox-oauth-test-private-", ".pem");
            Path publicKey = Files.createTempFile("mercadox-oauth-test-public-", ".pem");
            Files.writeString(privateKey, toPem("PRIVATE KEY", keyPair.getPrivate().getEncoded()),
                    StandardCharsets.UTF_8);
            Files.writeString(publicKey, toPem("PUBLIC KEY", keyPair.getPublic().getEncoded()),
                    StandardCharsets.UTF_8);
            privateKey.toFile().deleteOnExit();
            publicKey.toFile().deleteOnExit();
            return new JwtKeyFiles(privateKey, publicKey);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to create JWT keys for the application context test", exception);
        }
    }

    private static String toPem(String type, byte[] encodedKey) {
        String encoded = Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(encodedKey);
        return "-----BEGIN " + type + "-----\n"
                + encoded
                + "\n-----END " + type + "-----\n";
    }

    private record JwtKeyFiles(Path privateKey, Path publicKey) {
    }
}
