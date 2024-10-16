package com.pet_care.medical_prescription_service.dto.response;

import com.pet_care.medical_prescription_service.model.Pet;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetPrescriptionResponse {
    Pet pet;
    Set<MedicinePrescriptionResponse> medicines;
}
