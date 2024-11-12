package com.pet_care.customer_service.exception;

import com.pet_care.customer_service.dto.response.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for API exceptions and runtime exceptions.
 * It catches exceptions thrown in controllers and returns an appropriate response.
 */
@Slf4j
@ControllerAdvice
public class APIExceptionHandler {

    /**
     * Handles custom APIException and returns a response with the error code and message.
     * @param e The APIException to handle
     * @return ResponseEntity containing the APIResponse with the error code and message
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<?>> HandlingCustomerException(APIException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus()).body(APIResponse.<APIException>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    /**
     * Handles runtime exceptions and returns a response with the error code and message.
     * Logs the error for further inspection.
     * @param e The RuntimeException to handle
     * @return ResponseEntity containing the APIResponse with the error code and message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<RuntimeException>> HandlingRuntimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        ErrorCode errorCode = ErrorCode.valueOf(e.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(APIResponse.<RuntimeException>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
