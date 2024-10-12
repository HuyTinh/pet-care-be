package com.pet_care.employee_service.exception;

import com.pet_care.employee_service.dto.response.APIResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler {

    /**
     * @param e
     * @return
     */
    @NotNull
    @ExceptionHandler(APIException.class)
    public ResponseEntity<?> handleAPIException(@NotNull APIException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(APIResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
