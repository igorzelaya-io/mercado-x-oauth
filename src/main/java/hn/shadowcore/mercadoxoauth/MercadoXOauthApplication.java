package hn.shadowcore.mercadoxoauth;

import hn.shadowcore.mercadoxcontext.utils.JwtUtil;
import hn.shadowcore.mercadoxoauth.filter.JwtAuthFilter;
import hn.shadowcore.mercadoxoauth.filter.OrgIdContextFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = {
        "hn.shadowcore.mercadoxcontext.config",
        "hn.shadowcore.mercadoxlibrary"
})
@EntityScan(basePackages = "hn.shadowcore.mercadoxlibrary")
@EnableJpaRepositories(basePackages = "hn.shadowcore.mercadoxlibrary.jpa.repository")
public class MercadoXOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadoXOauthApplication.class, args);
    }

}
