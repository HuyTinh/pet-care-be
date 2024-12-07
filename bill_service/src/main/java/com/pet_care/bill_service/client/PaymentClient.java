package com.pet_care.bill_service.client;

import com.pet_care.bill_service.dto.request.PaymentRequest;
import com.pet_care.bill_service.dto.response.APIResponse;
import com.pet_care.bill_service.dto.response.CheckoutResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(name = "paymentClient", url = "http://localhost:8089/api/v1/payment-service")
public interface PaymentClient {

    @PostMapping("payment")
    APIResponse<CheckoutResponseData> getPaymentLink(@RequestBody PaymentRequest paymentRequest);
}
