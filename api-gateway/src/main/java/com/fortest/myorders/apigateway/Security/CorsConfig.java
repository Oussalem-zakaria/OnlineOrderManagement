package com.fortest.myorders.apigateway.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000"); // Autoriser React sur le port 3000
        corsConfig.addAllowedMethod("*"); // Autoriser toutes les méthodes HTTP (GET, POST, PUT, DELETE, etc.)
        corsConfig.addAllowedHeader("*"); // Autoriser tous les en-têtes
        corsConfig.setAllowCredentials(true); // Autoriser les cookies ou jetons d'authentification

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Appliquer la configuration à toutes les routes

        return new CorsWebFilter(source);
    }
}
