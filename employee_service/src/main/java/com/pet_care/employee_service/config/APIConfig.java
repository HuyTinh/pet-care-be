package com.pet_care.employee_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIConfig {

    /**
     * @return
     */
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
