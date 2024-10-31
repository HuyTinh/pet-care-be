package com.pet_care.customer_service.exception;

import com.pet_care.customer_service.dto.response.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class APIExceptionHandler {

    /**
     * @param e
     * @return
     */
    
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<?>> HandlingCustomerException( APIException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus()).body(APIResponse.<APIException>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    /**
     * @param e
     * @return
     */
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<RuntimeException>> HandlingRuntimeException( RuntimeException e) {
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
