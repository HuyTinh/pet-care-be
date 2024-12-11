package com.pet_care.appointment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A generic response wrapper class for API responses.
 * It includes status code, message, and data.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes fields with null values from the JSON output
@JsonIgnoreProperties(ignoreUnknown = true) // Ignores unknown properties during JSON deserialization
public class APIResponse<T> {

    // Default code for successful responses
    @Builder.Default
    int code = 1000;

    // Message to accompany the response, such as a success or error message
    String message;

    // The actual data returned in the response, represented as a generic type
    T data;
}
