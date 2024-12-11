package com.pet_care.employee_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A generic Data Transfer Object (DTO) used to standardize the API responses.
 * This class is used to wrap the response data, providing additional information
 * such as status code and message, to be returned from the server to the client.
 *
 * The generic type <T> allows this class to be flexible and return different types of data.
 */
@Getter  // Lombok annotation to generate getter methods for all fields
@Setter  // Lombok annotation to generate setter methods for all fields
@AllArgsConstructor  // Lombok annotation to generate a constructor with all fields
@NoArgsConstructor   // Lombok annotation to generate a no-argument constructor
@Builder  // Lombok annotation to implement the builder pattern for object creation
@FieldDefaults(level = AccessLevel.PRIVATE)  // Lombok annotation to set the default access level of fields to private
@JsonInclude(JsonInclude.Include.NON_NULL)  // Jackson annotation to exclude null fields from the JSON response
public class APIResponse<T> {

    // Default status code for successful response (1000 indicates success)
    @Builder.Default
    int code = 1000;

    // A message that provides additional information about the API response (e.g., success or error description)
    String message;

    // The actual data returned by the API (of type T, making this class flexible for different data types)
    T data;
}
