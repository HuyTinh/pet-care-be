package com.pet_care.appointment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 */
@Configuration
public class APIConfig implements WebMvcConfigurer {
    /**
     * @return
     */
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}