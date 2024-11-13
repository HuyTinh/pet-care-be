package com.pet_care.notification_service.config;

// Import necessary classes for JSON mapping and Spring configuration
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Marks this class as a configuration for Spring beans
public class APIConfig implements WebMvcConfigurer {

    /**
     * Provides a configured ObjectMapper bean for JSON serialization and deserialization.
     *
     * @return a new ObjectMapper instance
     */
    @Bean // Defines this method as a bean provider for dependency injection
    public ObjectMapper objectMapper() {
        return new ObjectMapper(); // Returns a new instance of ObjectMapper
    }
}
