package com.pet_care.notification_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIConfig {
    @NotNull
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
