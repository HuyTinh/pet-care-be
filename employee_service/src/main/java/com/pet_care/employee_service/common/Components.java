package com.pet_care.employee_service.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to define configuration components that are needed across the application.
 * Specifically, it defines a custom ObjectMapper bean to handle JSON serialization and deserialization.
 */
@Configuration  // Marks this class as a source of bean definitions for the Spring context.
public class Components {

    /**
     * This method creates an ObjectMapper bean to be managed by Spring's application context.
     * ObjectMapper is used for converting Java objects to JSON and vice versa.
     *
     * @return A new instance of ObjectMapper.
     */
    @Bean  // The @Bean annotation tells Spring to manage this method's return value as a bean.
    public ObjectMapper objectMapper() {
        // Creates a new ObjectMapper instance which will be used for JSON processing.
        return new ObjectMapper();
    }
}
