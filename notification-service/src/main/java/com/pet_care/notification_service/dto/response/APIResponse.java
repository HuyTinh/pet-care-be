package com.pet_care.notification_service.dto.response;

// Import necessary annotations for JSON serialization, Lombok, and class field configuration
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIResponse<T> {

    @Builder.Default // Sets the default value for the 'code' field when using the builder
    int code = 1000; // Default response code for successful operations

    String message; // Message providing details about the response

    T data; // Generic field to hold data of any type for the response
}
