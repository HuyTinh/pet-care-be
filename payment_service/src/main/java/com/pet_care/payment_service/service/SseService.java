package com.pet_care.payment_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    public final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    public void sendEventToClient(String clientId, String message) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("custom-event").data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(clientId);
            }
            emitter.complete();
        }
    }

    public void broadcastEvent(String message) {
        emitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("broadcast-event").data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(clientId);
            }
        });
    }
}
