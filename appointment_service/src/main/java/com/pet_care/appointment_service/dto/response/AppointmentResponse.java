package com.pet_care.appointment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

/**
 * Response object for appointment details.
 * It contains information about an appointment including the customer, pets, and services.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from the JSON response
public class AppointmentResponse {

    // Appointment ID
    Long id;

    // Account ID associated with the appointment
    @JsonProperty("account_id")
    Long accountId;

    // Account name (e.g., customer's full name or account name)
    String account;

    // Customer's first name
    @JsonProperty("first_name")
    String firstName;

    // Customer's last name
    @JsonProperty("last_name")
    String lastName;

    // Customer's email
    @JsonProperty("email")
    String email;

    // Customer's phone number
    @JsonProperty("phone_number")
    String phoneNumber;

    // Appointment date (e.g., the date when the appointment is scheduled)
    @JsonProperty("appointment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+07:00", pattern = "yyyy-MM-dd")
    Date appointmentDate;

    // Appointment time (specific time of the appointment)
    @JsonProperty("appointment_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "HH:mm")
    Date appointmentTime;

    // Status of the appointment (e.g., booked, checked-in, cancelled)
    AppointmentStatus status;

    // Pets associated with this appointment
    Set<PetResponse> pets;
}
