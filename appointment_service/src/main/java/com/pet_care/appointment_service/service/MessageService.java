package com.pet_care.appointment_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.config.WebSocketHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    @NotNull Queue<String> petQueue;

    @NotNull JmsTemplate jmsTemplate;

    @NotNull ObjectMapper objectMapper;

    @NotNull WebSocketHandler webSocketHandler;

    @NotNull WebSocketService webSocketService;


    /**
     * @param message
     */
    @JmsListener(destination = "receptionist-appointment-queue", containerFactory = "queueFactory")
    public void receiveMessage(String message) {
        petQueue.add(message);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param destination
     * @param appointment
     */
    public void sendMessage(@NotNull String destination, @NotNull String appointment) {
        jmsTemplate.convertAndSend(destination, appointment);
    }

    /**
     *
     */
    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        if (!petQueue.isEmpty()) {
            try {
                webSocketService.sendToAllCreateAppointment(petQueue.poll());
            } catch (Exception ignored) {
            }
        }
    }
}
