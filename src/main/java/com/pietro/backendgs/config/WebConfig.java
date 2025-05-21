package com.pietro.backendgs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                // Durante o desenvolvimento, permitir todas as origens
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("*")
                // Quando allowedOrigins é "*", não podemos definir allowCredentials como true
                .allowCredentials(false)
                .maxAge(3600);
                
        /* CONFIGURAÇÃO ALTERNATIVA - para uso com origens específicas e credenciais
        registry.addMapping("/**")
                .allowedOrigins(
                    // Origens de desenvolvimento local padrão
                    "http://localhost:8081", 
                    "http://127.0.0.1:8081", 
                    "http://localhost:3000",
                    // Origens do Expo Web
                    "http://localhost:19000",
                    "http://localhost:19006",
                    "http://localhost:19001",
                    "http://localhost:19002",
                    // Expo no dispositivo ou emulador (via seu IP)
                    "exp://localhost:19000",
                    "http://192.168.0.17:19000",
                    "http://192.168.0.17:19006",
                    "http://192.168.0.17:8081"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
        */
    }
} 