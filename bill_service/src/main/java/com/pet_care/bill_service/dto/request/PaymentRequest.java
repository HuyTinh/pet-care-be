package com.pet_care.bill_service.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.bill_service.dto.response.HospitalServiceResponse;
import com.pet_care.bill_service.dto.response.MedicinePrescriptionResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
    @JsonProperty("order_id")
    Long orderId;

    Set<HospitalServiceResponse> services;

    Set<MedicinePrescriptionResponse> medicines;

    @JsonProperty("total_money")
    Double totalMoney;
}
