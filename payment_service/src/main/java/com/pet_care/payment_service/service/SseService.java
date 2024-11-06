package com.pet_care.payment_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    public final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void sendEventToClient(Long orderId, boolean event) {
        SseEmitter emitter = emitters.get(orderId);
        System.out.println(orderId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(String.valueOf(orderId)).data(event));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(orderId);
            }
            emitter.complete();
        }
    }
}
