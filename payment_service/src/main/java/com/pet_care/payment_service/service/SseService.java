package com.pet_care.payment_service.service;

import com.pet_care.payment_service.client.BillClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SseService {

    BillClient billClient;

    public final Map<Long, SseEmitter> emitters;

    public void sendEventToClient(Long orderId, boolean event) {
        SseEmitter emitter = emitters.get(orderId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(String.valueOf(orderId)).data(event));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(orderId);
            }
            emitter.complete();
            emitter.onCompletion(() -> billClient.approveInvoice(orderId));
        }
    }
}
