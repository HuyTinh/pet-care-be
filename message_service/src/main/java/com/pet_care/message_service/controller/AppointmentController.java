package com.pet_care.message_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppointmentController {

    @SubscribeMapping("/queue/appointment")
    public void connect() {

    }
}
