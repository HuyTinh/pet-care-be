package com.pet_care.medical_prescription_service.exception;

import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(value = APIException.class)
    public ResponseEntity<APIResponse<?>> handleAPIException(APIException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(APIResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
