package com.pet_care.bill_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.bill_service.enums.InvoiceStatus;
import com.pet_care.bill_service.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceCreateRequest {

    @JsonProperty("prescription_id")
    Long prescriptionId;

    @Enumerated(EnumType.STRING)
    @JsonProperty("payment_method")
    PaymentMethod paymentMethod;

    InvoiceStatus status;

    @JsonProperty("total_money")
    Double totalMoney;
}
