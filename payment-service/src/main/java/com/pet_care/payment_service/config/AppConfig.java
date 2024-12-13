package com.pet_care.payment_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**") // Cho phép tất cả các endpoint
                    .allowedOrigins("http://127.0.0.1:5500","http://localhost:5173") // Chỉ định origin được phép
                    .allowedMethods("GET", "POST", "PUT", "DELETE") // Các HTTP method được phép
                    .allowCredentials(true); // Cho phép cookie hoặc chứng chỉ
        }
}
