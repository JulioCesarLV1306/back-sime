package com.sime.backwebsime.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Orígenes permitidos (usa patterns para https + subdominios si fuera necesario)
        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:4200",
            "https://web-sime.netlify.app",
            "https://back-sime.onrender.com"
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        ));
        
        // Todos los headers permitidos
        configuration.setAllowedHeaders(List.of("*"));
        
        // Permitir envío de cookies / credenciales
        configuration.setAllowCredentials(true);
        
        // Cache de preflight por 1 hora
        configuration.setMaxAge(3600L);

        // Aplica esta configuración a todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
