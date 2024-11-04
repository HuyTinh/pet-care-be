package com.pet_care.bill_service.client;

import com.pet_care.bill_service.dto.request.PaymentRequest;
import com.pet_care.bill_service.dto.response.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(name = "paymentClient", url = "http://localhost:8087/api/v1/payment-service")
public interface PaymentClient {

    @PostMapping("payment")
    APIResponse<Object> getPaymentLink(@RequestBody PaymentRequest paymentRequest);
}
