package com.pet_care.employee_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to define configuration components that are needed across the application.
 * Specifically, it defines a custom ObjectMapper bean to handle JSON serialization and deserialization.
 */
@Configuration  // Marks this class as a source of bean definitions for the Spring context.
public class AppConfig {

    /**
     * This method creates an ObjectMapper bean to be managed by Spring's application context.
     * ObjectMapper is used for converting Java objects to JSON and vice versa.
     *
     * @return A new instance of ObjectMapper.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Đăng ký các module cần thiết
        objectMapper.registerModules(
                new AfterburnerModule(), // Tăng hiệu suất serialize/deserialize
                new JavaTimeModule()     // Hỗ trợ LocalDate, LocalDateTime
        );

        // Cấu hình các chế độ serialize
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // ISO 8601 format
        return objectMapper;
    }
}
