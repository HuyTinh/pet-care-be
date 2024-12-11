package com.pet_care.notification_service.config;

// Import necessary classes for JSON mapping and Spring configuration
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.pet_care.notification_service.dto.response.AppointmentResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedList;
import java.util.Queue;

@Configuration // Marks this class as a configuration for Spring beans
public class AppConfig implements WebMvcConfigurer {

    /**
     * Provides a configured ObjectMapper bean for JSON serialization and deserialization.
     *
     * @return a new ObjectMapper instance
     */
    @Bean // Defines this method as a bean provider for dependency injection
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new AfterburnerModule()); // Returns a new instance of ObjectMapper
    }

    @Bean
    public Queue<AppointmentResponse> notificationAppointmentQueue() {
        return  new LinkedList<>()  ;
    }
}
