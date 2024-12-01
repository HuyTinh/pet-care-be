package com.pet_care.medical_prescription_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServiceConfig implements WebMvcConfigurer {

    /**
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new AfterburnerModule());
    }
}
