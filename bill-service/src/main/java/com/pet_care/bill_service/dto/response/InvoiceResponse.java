package com.pet_care.bill_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.bill_service.enums.InvoiceStatus;
import com.pet_care.bill_service.enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes fields with null values from the JSON output
public class InvoiceResponse {

    Long id;

    PrescriptionResponse prescription;

    @JsonProperty("total_money")
    Double totalMoney;

    @Enumerated(EnumType.STRING)
    @JsonProperty("payment_method")
    PaymentMethod paymentMethod;

    @Builder.Default
    InvoiceStatus status = InvoiceStatus.PENDING;

    @JsonProperty("checkout_response")
    Object checkoutResponse;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+07:00", pattern = "yyyy-MM-dd")
    Date createdAt;
}
