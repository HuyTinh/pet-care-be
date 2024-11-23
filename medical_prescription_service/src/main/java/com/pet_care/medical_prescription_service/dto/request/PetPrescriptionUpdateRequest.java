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
public class PetPrescriptionUpdateRequest {
    Long id;

    @JsonProperty("pet_id")
    Long petId;

    String note;

    String diagnosis;

    Set<PetMedicineUpdateRequest> petMedicines;
}