package com.pet_care.payment_service.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SseStore {
    @Bean
    public static Map<Integer, String> getSseSections(){
        return new HashMap<Integer, String>();
    }
}
