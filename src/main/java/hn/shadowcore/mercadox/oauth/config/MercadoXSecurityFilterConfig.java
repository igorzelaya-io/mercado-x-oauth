package hn.shadowcore.mercadox.oauth.config;

<<<<<<< Updated upstream
import hn.shadowcore.mercadoxcontext.filter.JwtAuthFilter;
import hn.shadowcore.mercadoxcontext.filter.OrgIdContextFilter;
=======
import hn.shadowcore.mercadox.context.filter.JwtAuthFilter;
import hn.shadowcore.mercadox.context.filter.TenantValidatorFilter;
import hn.shadowcore.mercadox.context.security.JwtVerifier;
import hn.shadowcore.mercadox.context.validator.AnonymousTenantValidator;
>>>>>>> Stashed changes
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Import({JwtAuthFilter.class, OrgIdContextFilter.class})
public class MercadoXSecurityFilterConfig {

<<<<<<< Updated upstream
    private final JwtAuthFilter jwtAuthFilter;
=======
    private final JwtVerifier jwtVerifier;
>>>>>>> Stashed changes

    private final OrgIdContextFilter orgIdContextFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
<<<<<<< Updated upstream
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(orgIdContextFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
=======
                        .requestMatchers("/api/v1/public/**", "/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new TenantValidatorFilter(jwtVerifier, anonymousTenantValidator), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthFilter(jwtVerifier), TenantValidatorFilter.class).build();

>>>>>>> Stashed changes
    }

}
