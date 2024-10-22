package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionHomeDashboardResponse {
    Long prescriptionId;
    CustomerPrescriptionResponse customerPrescription;
    PetPrescriptionResponse petPrescription;
    ProfilesDoctorResponse profilesDoctor;
    Set<PrescriptionDetailResponse> prescriptionDetails;
}
