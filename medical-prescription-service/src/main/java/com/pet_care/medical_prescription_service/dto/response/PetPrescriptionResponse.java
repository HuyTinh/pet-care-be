package com.pet_care.medical_prescription_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetPrescriptionResponse {
    Long id;
    PetResponse pet;
    String diagnosis;
    Set<PetMedicineResponse> medicines;
    @JsonProperty("veterinary_cares")
    Set<PetVeterinaryCareResponse> veterinaryCares;
}
