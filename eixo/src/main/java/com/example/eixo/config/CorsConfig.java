package com.example.eixo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS de toda a API num só lugar.
 * As origens permitidas vêm de eixo.cors.origens (variável CORS_ORIGENS), separadas por vírgula.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${eixo.cors.origens}")
    private String[] origensPermitidas;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origensPermitidas)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
