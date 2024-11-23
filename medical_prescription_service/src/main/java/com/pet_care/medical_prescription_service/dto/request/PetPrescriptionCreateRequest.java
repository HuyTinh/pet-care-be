package com.pet_care.medical_prescription_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medical_prescription_service.model.PetVeterinaryCare;
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
public class PetPrescriptionCreateRequest {
    @JsonProperty("pet_id")
    Long petId;

    String diagnosis;

    Set<PetMedicineCreateRequest> petMedicines;

    Set<PetVeterinaryCareCreateRequest> petVeterinaryCares;
}
