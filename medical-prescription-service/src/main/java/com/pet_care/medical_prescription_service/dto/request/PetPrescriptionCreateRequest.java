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
public class PetPrescriptionCreateRequest {
    @JsonProperty("pet_id")
    Long petId;

    String diagnosis;

    @JsonProperty("pet_medicines")
    Set<PetMedicineCreateRequest> petMedicines;


    @JsonProperty("pet_veterinary_cares")
    Set<PetVeterinaryCareCreateRequest> petVeterinaryCares;
}
