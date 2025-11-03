package org.upc.gatewayservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
                .authorizeExchange(exchange -> exchange
                        // --- RUTAS PÚBLICAS ---
                        .pathMatchers(
                                "/api/v1/authentication/**",
                                "/api/v1/jwks/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",                // 👈 necesario para recursos estáticos
                                "/v3/api-docs/**",            // 👈 necesario para el spec JSON
                                "/*/v3/api-docs/**",          // 👈 docs de microservicios vía gateway
                                "/swagger-resources/**",      // 👈 para swagger-config interno
                                "/swagger-config/**",          // 👈 algunas versiones lo usan
                                "/actuator/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }
}
