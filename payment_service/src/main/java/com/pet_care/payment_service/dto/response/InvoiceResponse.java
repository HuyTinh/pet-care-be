package com.pet_care.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.payment_service.enums.InvoiceStatus;
import com.pet_care.payment_service.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {

    Long id;

    PrescriptionResponse prescription;

    @JsonProperty("total_money")
    Double totalMoney;

    @JsonProperty("payment_method")
    PaymentMethod paymentMethod;

    InvoiceStatus status;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+07:00", pattern = "yyyy-MM-dd")
    Date createdAt;
}
