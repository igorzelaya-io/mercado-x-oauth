package hn.alturaforge.mercadox.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages = {
        "hn.alturaforge.mercadox.oauth",
        "hn.alturaforge.mercadox.context",
        "hn.alturaforge.mercadox.library"
})
public class MercadoXOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadoXOauthApplication.class, args);
    }

}
