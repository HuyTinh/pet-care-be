package com.pet_care.medicine_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A generic API response wrapper used for standardizing API responses.
 *
 * @param <T> the type of the response data
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

    /**
     * The response code, default is 1000.
     */
    @Builder.Default
    int code = 1000;

    /**
     * A message to be included in the response (optional).
     */
    String message;

    /**
     * The actual data to be returned in the response (optional).
     */
    T data;
}
