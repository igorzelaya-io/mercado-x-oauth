package hn.shadowcore.mercadox;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = {
        "hn.shadowcore.mercadoxcontext.config",
        "hn.shadowcore.mercadoxlibrary"
})
@EntityScan(basePackages = "hn.shadowcore.mercadoxlibrary")
public class MercadoXOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadoXOauthApplication.class, args);
    }

}
