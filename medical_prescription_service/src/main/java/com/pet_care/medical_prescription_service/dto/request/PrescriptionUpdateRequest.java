package com.pet_care.medical_prescription_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionUpdateRequest {
    Long id;

    Set<String> services;

    Set<PetPrescriptionCreateRequest> details;

    @JsonProperty("total_money")
    Double totalMoney;
}