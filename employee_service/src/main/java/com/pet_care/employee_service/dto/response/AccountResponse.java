package com.pet_care.employee_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object (DTO) class used for representing the response structure
 * when sending back account-related information to the client.
 * This is used to send a simplified view of the account data, typically in response to an API request.
 */
@Getter  // Lombok annotation to generate getter methods for all fields
@Setter  // Lombok annotation to generate setter methods for all fields
@Builder  // Lombok annotation to implement the builder pattern for object creation
@AllArgsConstructor  // Lombok annotation to generate a constructor with all fields
@NoArgsConstructor   // Lombok annotation to generate a no-argument constructor
@FieldDefaults(level = AccessLevel.PRIVATE)  // Lombok annotation to set the default access level of fields to private
@JsonIgnoreProperties(ignoreUnknown = true)  // Jackson annotation to ignore any unknown properties in the incoming JSON
public class AccountResponse {

    // Unique identifier of the account
    Long id;

    // Email address associated with the account
    String email;
}
