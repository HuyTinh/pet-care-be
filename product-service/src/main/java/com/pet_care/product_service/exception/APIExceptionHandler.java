package com.pet_care.product_service.exception;

import com.pet_care.product_service.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler to catch API exceptions.
 */
@ControllerAdvice
public class APIExceptionHandler {

    /**
     * Handles custom API exceptions and sends a structured response.
     *
     * @param e the exception thrown
     * @return the response entity with error details
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<?>> handleAPIException(APIException e) {
        // Extract the error code from the exception
        ErrorCode errorCode = e.getErrorCode();

        // Return the error details in a standardized API response
        return ResponseEntity.status(errorCode.getStatus()).body(APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
}
