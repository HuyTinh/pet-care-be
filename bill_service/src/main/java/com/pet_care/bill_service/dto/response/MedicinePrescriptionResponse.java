package com.pet_care.medical_prescription_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicinePrescriptionResponse {
    Long id;
    String name;
    Long quantity;
    @JsonProperty("calculate_unit")
    String calculateUnit;

    @JsonProperty("total_money")
    Double totalMoney;
}
