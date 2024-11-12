package com.pet_care.customer_service.dto.request;

// Import for JSON field name customization
import com.fasterxml.jackson.annotation.JsonProperty;
// Lombok imports for reducing boilerplate code
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for creating an appointment request.
 * This class holds the necessary information about the customer and the appointment details.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentCreateRequest {

    /**
     * Email of the customer, used to identify or communicate with them.
     */
    String email;

    /**
     * Customer's first name.
     * Serialized as "first_name" to match the expected JSON structure.
     */
    @JsonProperty("first_name")
    String firstName;

    /**
     * Customer's last name.
     * Serialized as "last_name" to match the expected JSON structure.
     */
    @JsonProperty("last_name")
    String lastName;

    /**
     * Customer's phone number, used for potential contact or identification.
     * Serialized as "phone_number" to match the expected JSON structure.
     */
    @JsonProperty("phone_number")
    String phoneNumber;

    /**
     * Unique identifier for the customer's account.
     * Serialized as "account_id" to match the expected JSON structure.
     * Helps link the appointment to a specific account.
     */
    @JsonProperty("account_id")
    Long accountId;

    /**
     * Detailed information about the appointment.
     * This field is another DTO encapsulating appointment-specific details,
     * such as date, time, and service type.
     */
    AppointmentRequest appointment;
}
