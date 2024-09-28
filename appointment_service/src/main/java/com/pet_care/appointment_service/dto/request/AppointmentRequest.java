package com.pet_care.appointment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class AppointmentRequest {
    @JsonProperty("customer_id")
    Long customerId;

    Set<String> services;

    @JsonProperty("appointment_date")
    Date appointmentDate;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    Set<PetCreateRequest> pets;
}
