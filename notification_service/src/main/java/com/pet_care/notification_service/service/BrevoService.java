package com.pet_care.notification_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.notification_service.client.BrevoClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrevoService {

    RestClient restClient;

    @JmsListener(destination = "appointment-success-notification-queue")
    public void sendAppointmentSuccessfulEmail(String message) throws JsonProcessingException {
        restClient.post().body(message).retrieve();
    }

}
