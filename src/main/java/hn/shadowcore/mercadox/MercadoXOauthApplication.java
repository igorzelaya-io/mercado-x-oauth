package hn.shadowcore.mercadox;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MercadoXOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadoXOauthApplication.class, args);
    }

}
