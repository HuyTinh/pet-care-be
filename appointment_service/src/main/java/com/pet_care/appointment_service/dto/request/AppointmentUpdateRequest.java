package com.pet_care.appointment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.model.HospitalServiceEntity;
import com.pet_care.appointment_service.model.Pet;
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
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentUpdateRequest {
    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("email")
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("account_id")
    Long accountId;

    @JsonProperty("appointment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date appointmentDate;

    @JsonProperty("appointment_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    Date appointmentTime;

    Set<Pet> pets;

    Set<HospitalServiceEntity> services;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;
}
