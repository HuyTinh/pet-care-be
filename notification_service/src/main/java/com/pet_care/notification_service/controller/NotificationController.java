package com.pet_care.notification_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.notification_service.service.BrevoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("notification")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    BrevoService brevoService;

    ObjectMapper objectMapper;

    @GetMapping
    public void notification() throws JsonProcessingException {
        Object object = "{'name':'ba' ," +
                "" +
                "'nha': 'bon' }";
        System.out.println(objectMapper.writeValueAsString(object));
        brevoService.sendEmail();
    }
}
