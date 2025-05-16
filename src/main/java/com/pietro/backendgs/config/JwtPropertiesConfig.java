package com.pietro.backendgs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:jwt.properties")
public class JwtPropertiesConfig {
    // This class loads the JWT properties from the jwt.properties file
} 