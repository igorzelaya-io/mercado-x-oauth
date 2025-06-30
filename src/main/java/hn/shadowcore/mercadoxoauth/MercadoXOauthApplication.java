package hn.shadowcore.mercadoxoauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "hn.shadowcore.mercadoxlibrary",
        "hn.shadowcore.mercadoxoauth"
})
@EnableJpaRepositories(basePackages = "hn.shadowcore.mercadoxlibrary.jpa.repository")
@EntityScan(basePackages = "hn.shadowcore.mercadoxlibrary.entity.model")
public class MercadoXOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadoXOauthApplication.class, args);
    }

}
