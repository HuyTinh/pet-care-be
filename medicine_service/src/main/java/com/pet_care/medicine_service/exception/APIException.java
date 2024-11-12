package com.pet_care.medicine_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Custom exception class for handling API errors.
 */
@Getter
@AllArgsConstructor
public class APIException extends RuntimeException {

    /**
     * Error code associated with the exception.
     */
    private final ErrorCode errorCode;
}
