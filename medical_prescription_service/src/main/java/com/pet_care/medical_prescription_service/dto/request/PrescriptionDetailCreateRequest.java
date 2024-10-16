package com.pet_care.medical_prescription_service.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionDetailCreateRequest {

    @JsonProperty("medicine_id")
    Long medicineId;

    Long quantity;

    @JsonProperty("calculation_id")
    Long calculationId;

    @JsonProperty("total_money")
    Double totalMoney;
}

