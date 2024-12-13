package com.pet_care.bill_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetMedicineResponse {
    Long id;
    String name;
    Long quantity;
    @JsonProperty("calculate_unit")
    String calculateUnit;

    String note;

    @JsonProperty("total_money")
    Double totalMoney;

}
