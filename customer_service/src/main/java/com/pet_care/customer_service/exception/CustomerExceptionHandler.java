package com.pet_care.customer_service.exception;

import com.pet_care.customer_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<ApiResponse<CustomerException>> HandlingCustomerException(CustomerException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.<CustomerException>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> HandlingRuntimeException(RuntimeException e) {
        ErrorCode errorCode = ErrorCode.valueOf(e.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
}
