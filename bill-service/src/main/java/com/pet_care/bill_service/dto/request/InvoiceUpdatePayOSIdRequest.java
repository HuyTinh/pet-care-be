package com.pet_care.bill_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceUpdatePayOSIdRequest {
    Long invoiceId;
    String payOSId;
}
