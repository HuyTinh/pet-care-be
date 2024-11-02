package com.pet_care.payment_service.controller;

import com.pet_care.payment_service.service.PayOSService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PayOSService payOSService;

    @PostMapping
    public ResponseEntity<String> getPaymentLink() throws Exception {
        return  ResponseEntity.ok(payOSService.createPaymentLink());
    }
}
