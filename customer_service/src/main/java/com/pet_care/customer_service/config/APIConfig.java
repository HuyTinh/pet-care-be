package com.pet_care.customer_service.config;

// Import necessary libraries for JSON handling and Spring configuration
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Marks this class as a configuration class
@Configuration
public class APIConfig implements WebMvcConfigurer {

    /**
     * Provides a configured ObjectMapper bean for JSON processing.
     *
     * @return a new instance of ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        // Return a new instance of ObjectMapper, used for JSON serialization and deserialization
        return new ObjectMapper();
    }
}
