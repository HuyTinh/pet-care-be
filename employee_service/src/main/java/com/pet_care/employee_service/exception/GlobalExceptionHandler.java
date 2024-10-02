package com.pet_care.employee_service.exception;

import com.pet_care.employee_service.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<APIException>> HandlingCustomerException(APIException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus()).body(APIResponse.<APIException>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

}
