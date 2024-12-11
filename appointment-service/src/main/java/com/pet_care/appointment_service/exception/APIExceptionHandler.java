package com.pet_care.appointment_service.exception;

import com.pet_care.appointment_service.dto.response.APIResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for API-related exceptions.
 * This class handles exceptions thrown in any controller and returns a standardized response.
 */
@ControllerAdvice
public class APIExceptionHandler {

    /**
     * Handles APIException (custom runtime exception) and returns a structured error response.
     *
     * @param e The exception that was thrown
     * @return A ResponseEntity containing an APIResponse with error details
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<?>> handleAPIException(@NotNull APIException e) {
        // Get the error code from the exception
        ErrorCode errorCode = e.getErrorCode();

        // Return the error response with the status and code from the error code
        return ResponseEntity
                .status(errorCode.getStatus())  // Set HTTP status from the error code
                .body(APIResponse.builder()     // Create an APIResponse object with error details
                        .code(errorCode.getCode()) // Set the error code
                        .message(errorCode.getMessage()) // Set the error message
                        .build());
    }

    /**
     * Handles any other runtime exceptions that are not specifically handled.
     *
     * @param e The runtime exception
     * @return A ResponseEntity with a generic error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<?>> handleRuntimeException(@NotNull RuntimeException e) {
        // Return a generic error response for unhandled runtime exceptions
        return ResponseEntity
                .status(500)  // Internal Server Error
                .body(APIResponse.builder()
                        .code(1001) // A generic error code
                        .message("An unexpected error occurred")
                        .build());
    }
}
