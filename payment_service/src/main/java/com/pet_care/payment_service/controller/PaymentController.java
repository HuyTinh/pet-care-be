package com.pet_care.payment_service.controller;

import com.pet_care.payment_service.dto.request.PaymentRequest;
import com.pet_care.payment_service.dto.request.WebhookRequest;
import com.pet_care.payment_service.dto.response.APIResponse;
import com.pet_care.payment_service.service.PayOSService;
import com.pet_care.payment_service.service.SseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;

import java.util.Map;


@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PayOSService payOSService;

    SseService sseService;

    /**
     * @param webhookRequest
     * @return
     * @throws Exception
     */
    @PostMapping()
    public APIResponse<CheckoutResponseData> getPaymentLink(@RequestBody PaymentRequest paymentRequest) throws Exception {
        return  APIResponse.<CheckoutResponseData>builder()
                .data(payOSService.createPaymentQRCode(paymentRequest))
                .build();
    }

    /**
     * @param orderId
     * @return
     * @throws Exception
     */
    @PostMapping("{orderId}/cancel")
    public APIResponse<?> cancelPayment(@PathVariable("orderId") Integer orderId) throws Exception {
      Integer cancelSuccess = payOSService.cancelPaymentLink(orderId);
      String message = "Cancelled Fail";
        if(cancelSuccess == 1){
            message = "Cancelled Successfully";
        }
        return APIResponse.builder()
                .message(message)
                .build();
    }

    /**
     * @param webhookRequest
     * @throws Exception
     */
    @PostMapping("/confirm")
    public void checkOutSuccessfully(@RequestBody WebhookRequest webhookRequest) throws Exception {
        sseService.sendEventToClient(
                payOSService.getOrderCode(
                        webhookRequest
                ), true);
    }

}
