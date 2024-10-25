package com.pet_care.manager_service.dto.response;

import com.pet_care.manager_service.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentHomeDashboardTableResponse {
    Long appointmentId;
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    CustomerPrescriptionResponse customerPrescriptionResponse;
    Set<PetPrescriptionResponse> petPrescriptionResponses;
    ProfilesDoctorResponse profilesDoctorResponse;
    AppointmentStatus appointmentStatus;
    Set<PrescriptionResponse> prescriptionResponses;
}
