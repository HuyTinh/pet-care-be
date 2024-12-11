package com.pet_care.medical_prescription_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
