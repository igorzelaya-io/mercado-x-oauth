package hn.alturaforge.mercadox.oauth.config;

import hn.alturaforge.mercadox.context.config.JwtConfig;
import hn.alturaforge.mercadox.oauth.service.OAuthTenantValidatorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        JwtConfig.class,
        OAuthTenantValidatorService.class
})
public class OAuthSharedConfiguration { }