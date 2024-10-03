package com.pet_care.notification_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BrevoClient {

    @Value("${brevo.url}")
    private String brevoUrl;

    @Value("${brevo.key}")
    private String brevoKey;

    @Bean
    public RestClient client() {
        return RestClient.create().mutate().baseUrl(brevoUrl)
                .defaultHeader("api-key", brevoKey)
                .defaultHeader("Content-Type", "application/json; charset=UTF-8").build();
    }


}
