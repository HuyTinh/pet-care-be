package com.pet_care.employee_service.exception;

import com.pet_care.employee_service.dto.response.APIResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling API-specific exceptions.
 * This class uses Spring's @ControllerAdvice to handle exceptions across the entire application.
 * It specifically handles APIException and customizes the response returned to the client.
 */
@ControllerAdvice
public class APIExceptionHandler {

    /**
     * Handles APIException thrown in the application.
     * @param e the APIException that was thrown
     * @return a ResponseEntity containing the APIResponse with error code and message
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<?> handleAPIException(@NotNull APIException e) {
        // Retrieve the error code from the exception
        ErrorCode errorCode = e.getErrorCode();

        // Return a ResponseEntity with an appropriate HTTP status and an APIResponse body
        return ResponseEntity.status(errorCode.getStatus())
                .body(APIResponse.builder()
                        .code(errorCode.getCode())  // The error code from the ErrorCode enum
                        .message(errorCode.getMessage())  // The corresponding error message
                        .build());
    }
}
