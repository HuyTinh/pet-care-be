package com.pet_care.appointment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.entity.Pet;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

/**
 * DTO class for updating an appointment request. It contains all necessary
 * details required to modify an existing appointment.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentUpdateRequest {

    // First name of the person updating the appointment
    @JsonProperty("first_name")
    String firstName;

    // Last name of the person updating the appointment
    @JsonProperty("last_name")
    String lastName;

    // Email of the person updating the appointment
    @JsonProperty("email")
    String email;

    // Phone number of the person updating the appointment
    @JsonProperty("phone_number")
    String phoneNumber;

    // Account ID associated with the appointment
    @JsonProperty("account_id")
    Long accountId;

    // Date of the appointment to be updated in 'yyyy-MM-dd' format
    @JsonProperty("appointment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+07:00", pattern = "yyyy-MM-dd")
    Date appointmentDate;

    // Time of the appointment to be updated in 'HH:mm' format
    @JsonProperty("appointment_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "HH:mm")
    Date appointmentTime;

    // Set of pets associated with the appointment
    Set<Pet> pets;

    // Status of the appointment (e.g., pending, confirmed, etc.)
    @Enumerated(EnumType.STRING)
    AppointmentStatus status;
}
