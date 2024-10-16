package com.pet_care.medical_prescription_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetPrescriptionCreateRequest {
    Long petId;

    String note;

    Set<PrescriptionDetailCreateRequest> medicines;
}
