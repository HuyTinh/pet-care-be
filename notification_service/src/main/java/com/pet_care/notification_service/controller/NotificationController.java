package com.pet_care.notification_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notification")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    ObjectMapper mapper;

    @PostMapping("result")
    public void testBrevoWebHook(@ModelAttribute Object object) throws JsonProcessingException {
        System.out.println(mapper.writeValueAsString(object));
    }
}
