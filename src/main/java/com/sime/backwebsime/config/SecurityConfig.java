package com.sime.backwebsime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Habilita CORS usando nuestro bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // Deshabilita CSRF si tu API es stateless (ajusta según necesites)
            .csrf(csrf -> csrf.disable())
            // Permite acceso libre a tu API y Swagger/OpenAPI
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
