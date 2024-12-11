package com.pet_care.customer_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

/**
 * DTO representing an appointment request.
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRequest {

    /**
     * Unique identifier for the customer who booked the appointment,
     * serialized as "customer_id" to match external API format.
     */
    @JsonProperty("customer_id")
    Long customerId;

    /**
     * Set of services selected by the customer for the appointment,
     * represented as a set of service names or IDs for flexibility.
     */
    Set<String> services;

    /**
     * Date of the appointment, expected in "yyyy-MM-dd" format.
     * Serialized as "appointment_date" to ensure compatibility with client data.
     * The date is managed independently of the appointment time.
     */
    @JsonProperty("appointment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date appointmentDate;

    /**
     * Time of the appointment, expected in "HH:mm" format.
     * Serialized as "appointment_time" for standardized API interaction.
     * Only the time portion is persisted due to @Temporal(TemporalType.TIME).
     */
    @JsonProperty("appointment_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Temporal(TemporalType.TIME)
    Date appointmentTime;

    /**
     * Current status of the appointment, such as "scheduled," "completed," or "canceled."
     * Provides a clear indication of the appointment lifecycle for client interaction.
     */
    String status;

    /**
     * Set of pets that will be part of the appointment, represented as a set of
     * PetCreateRequest objects to hold individual pet details.
     * Allows customers to specify multiple pets for a single appointment.
     */
    Set<PetCreateRequest> pets;
}
