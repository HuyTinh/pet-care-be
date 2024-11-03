package com.pet_care.payment_service.controller;

import com.pet_care.payment_service.dto.response.WebhookRequest;
import com.pet_care.payment_service.service.PayOSService;
import com.pet_care.payment_service.service.SseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PayOSService payOSService;

    SseService sseService;

    @PostMapping
    public ResponseEntity<?> getPaymentLink() throws Exception {
        return  ResponseEntity.ok(payOSService.createPaymentQRCode());
    }

    @PostMapping("/confirm")
    public void checkOutSuccessfully(@RequestBody WebhookRequest webhookRequest) throws Exception {
//        System.out.println(webhookRequest.getData());
        sseService.sendEventToClient("channel1", "Chào");
    }

}
