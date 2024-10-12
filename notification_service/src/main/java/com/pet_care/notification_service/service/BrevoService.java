package com.pet_care.notification_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrevoService {

    @NotNull RestClient restClient;

    /**
     * @param message
     * @throws JsonProcessingException
     */
    @JmsListener(destination = "appointment-success-notification-queue")
    public void sendAppointmentSuccessfulEmail(@NotNull String message) throws JsonProcessingException {
        restClient.post().body(message).retrieve();
    }

}
