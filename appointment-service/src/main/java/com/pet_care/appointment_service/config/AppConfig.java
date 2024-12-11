package com.pet_care.appointment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for API-related beans and settings.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * Bean to configure the ObjectMapper used for JSON serialization and deserialization.
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
