package com.pet_care.bill_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.bill_service.enums.InvoiceStatus;
import com.pet_care.bill_service.enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {

    Long id;

    @JsonProperty("customer_id")
    Long customerId;

    @JsonProperty("")
    Long prescriptionId;

    Long appointmentId;

    Double prescriptionAmount;

    Double appointmentAmount;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    InvoiceStatus status;
}
