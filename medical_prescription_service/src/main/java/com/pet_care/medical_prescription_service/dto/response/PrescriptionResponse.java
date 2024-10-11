package com.pet_care.medical_prescription_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
import com.pet_care.medical_prescription_service.model.Appointment;
import com.pet_care.medical_prescription_service.model.PetPrescription;
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

    @JsonProperty("appointment_id")
    Appointment appointment;

    @JsonProperty("details")
    Set<PetPrescription> prescriptionDetails;

    @Enumerated(EnumType.STRING)
    PrescriptionStatus status;

    @JsonProperty("amount")
    Double prescriptionAmount;
}
