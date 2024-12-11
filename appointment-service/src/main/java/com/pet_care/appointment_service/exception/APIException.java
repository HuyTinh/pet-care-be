package com.pet_care.appointment_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * Custom exception to handle API-related errors with a specific error code.
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class APIException extends RuntimeException {

    /**
     * The error code associated with the exception.
     */
    ErrorCode errorCode;
}
