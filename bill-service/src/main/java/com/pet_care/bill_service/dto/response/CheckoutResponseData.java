package com.pet_care.bill_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutResponseData {
        String bin;
        String accountNumber;
        String accountName;
        Integer amount;
        String description;
        Long orderCode;
        String currency;
        String paymentLinkId;
        String status;
        Long expiredAt;
        String checkoutUrl;
        String qrCode;
}
