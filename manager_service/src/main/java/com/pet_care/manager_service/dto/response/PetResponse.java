package com.pet_care.manager_service.dto.response;

import com.pet_care.manager_service.entity.Prescription;
import com.pet_care.manager_service.entity.Species;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse {
    Long id;
    String name;
    String age;
    double weight;
    SpeciesResponse speciesResponse;
    Set<PrescriptionResponse> prescriptionResponses;
}
