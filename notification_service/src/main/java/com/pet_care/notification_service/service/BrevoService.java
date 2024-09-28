package com.pet_care.notification_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.notification_service.client.BrevoClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrevoService {

    RestClient restClient;

    ObjectMapper objectMapper;

    public void sendEmail() throws JsonProcessingException {
        restClient.post().body("{ \"to\": [ { \"email\": \"tinhnth15112003@gmail.com\", \"name\": \"Hồng\" } ], \"subject\": \"Appointment Confirmation!\", \"params\": { \"username\": \"Hồng Ánh\", \"appointment_date\": \"2024-09-29\", \"appointment_time\": \"10:00\" }, \"templateId\": 1 }").retrieve();
    }

}
