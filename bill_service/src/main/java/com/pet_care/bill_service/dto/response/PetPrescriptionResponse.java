package com.pet_care.bill_service.dto.response;

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
    Long id;
    PetResponse pet;
    String note;
    String diagnosis;
    Set<MedicinePrescriptionResponse> medicines;
}
