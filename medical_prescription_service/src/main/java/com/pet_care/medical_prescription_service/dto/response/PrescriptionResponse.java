package com.pet_care.medical_prescription_service.dto.response;

import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
import com.pet_care.medical_prescription_service.model.Appointment;
import com.pet_care.medical_prescription_service.model.PrescriptionDetail;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionResponse {
    Long id;

    Appointment appointment;

    @Enumerated(EnumType.STRING)
    PrescriptionStatus status;

    Set<PrescriptionDetail> prescriptionDetails;
}
