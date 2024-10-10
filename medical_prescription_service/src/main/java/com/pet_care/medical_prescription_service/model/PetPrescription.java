package com.pet_care.medical_prescription_service.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetPrescription {
    Pet pet;
    Set<Medicine> medicines;
}
