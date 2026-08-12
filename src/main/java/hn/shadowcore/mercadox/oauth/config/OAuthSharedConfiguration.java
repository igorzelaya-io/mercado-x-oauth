package hn.shadowcore.mercadox.oauth.config;

import hn.shadowcore.mercadox.context.config.JwtConfig;
import hn.shadowcore.mercadox.oauth.service.OAuthTenantValidatorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        JwtConfig.class,
        OAuthTenantValidatorService.class
})
public class OAuthSharedConfiguration { }