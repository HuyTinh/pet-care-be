package com.pet_care.customer_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Custom exception class that represents an API error.
 * Extends RuntimeException to be thrown as a runtime exception.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class APIException extends RuntimeException {

    // The error code associated with this exception.
    ErrorCode errorCode;
}
