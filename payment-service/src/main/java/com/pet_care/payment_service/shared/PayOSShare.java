package com.pet_care.payment_service.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vn.payos.PayOS;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PayOSShare {
    @Value("${payos.client-id}")
    String clientId;

    @Value("${payos.api-key}")
    String apiKey;

    @Value("${payos.checksum-key}")
    String checksumKey;

    @Bean
    public final PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }

    @Bean
    public final Map<Long, SseEmitter> getMapSseEmitter() {
        return new ConcurrentHashMap<>();
    }

}
