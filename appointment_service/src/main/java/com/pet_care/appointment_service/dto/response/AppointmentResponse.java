package com.pet_care.appointment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {
    Long id;

    CustomerResponse customer;

    @JsonProperty("appointment_date")
    Date appointmentDate;

    Set<HospitalServiceResponse> services;

    AppointmentStatus status;

    Set<PetResponse> pets;

    Date created_at;
}
