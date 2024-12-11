package com.pet_care.medical_prescription_service.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetMedicineCreateRequest {

    @JsonProperty("medicine_id")
    Long medicineId;

    @JsonProperty("calculation_id")
    Long calculationId;

    Long quantity;

    @JsonProperty("total_money")
    Double totalMoney;

    String note;
}

