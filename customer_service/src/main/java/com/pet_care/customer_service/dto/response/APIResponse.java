package com.pet_care.customer_service.dto.response;

// Import necessary annotations for JSON handling and Lombok usage
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A generic API response wrapper for standardized API responses.
 * This class is used to wrap the response data, including a status code and message.
 * It can hold any type of data in the 'data' field.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

    /**
     * Default response code (1000 indicates success).
     * The value can be customized in response creation.
     */
    @Builder.Default
    int code = 1000;

    /**
     * A message providing additional information about the response.
     */
    String message;

    /**
     * The data being returned in the response.
     * The data type is generic, so it can be any type.
     */
    T data;
}
