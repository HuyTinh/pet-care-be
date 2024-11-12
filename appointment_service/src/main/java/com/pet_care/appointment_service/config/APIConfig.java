package com.pet_care.appointment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for API-related beans and settings.
 */
@Configuration
public class APIConfig implements WebMvcConfigurer {

    /**
     * Bean to configure the ObjectMapper used for JSON serialization and deserialization.
     *
     * @return A new instance of ObjectMapper.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
