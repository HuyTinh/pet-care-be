package com.pet_care.payment_service.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vn.payos.PayOS;

@Component
public class PayOSShare {
    @Value("${PAYOS_CLIENT_ID}")
    String clientId;

    @Value("${PAYOS_API_KEY}")
    String apiKey;

    @Value("${PAYOS_CHECKSUM_KEY}")
    String checksumKey;

    @Bean
    public final PayOS getPayOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}
