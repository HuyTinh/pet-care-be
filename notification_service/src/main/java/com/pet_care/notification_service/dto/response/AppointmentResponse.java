package com.pet_care.notification_service.dto.response;

// Import necessary annotations for JSON serialization, field configuration, and custom data types
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.notification_service.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {

    Long id; // Unique identifier for the appointment

    @JsonProperty("account_id") // Maps JSON field "account_id" to this field
    Long accountId; // Identifier for the account associated with the appointment

    String account; // Account name or identifier for quick reference

    @JsonProperty("first_name") // Maps JSON field "first_name" to this field
    String firstName; // First name of the person associated with the appointment

    @JsonProperty("last_name") // Maps JSON field "last_name" to this field
    String lastName; // Last name of the person associated with the appointment

    @JsonProperty("email") // Maps JSON field "email" to this field
    String email; // Email of the person associated with the appointment

    @JsonProperty("phone_number") // Maps JSON field "phone_number" to this field
    String phoneNumber; // Phone number of the person associated with the appointment

    @JsonProperty("appointment_date") // Maps JSON field "appointment_date" to this field
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+07:00", pattern = "yyyy-MM-dd")
    // Formats date as a string in the specified timezone and date pattern
    Date appointmentDate; // Date of the appointment

    @JsonProperty("appointment_time") // Maps JSON field "appointment_time" to this field
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "HH:mm")
    // Formats time as a string in the specified timezone and time pattern
    Date appointmentTime; // Time of the appointment

    AppointmentStatus status; // Status of the appointment, represented by an enum

    Set<HospitalServiceResponse> services; // Set of hospital services associated with the appointment

    Set<PetResponse> pets; // Set of pets associated with the appointment
}
