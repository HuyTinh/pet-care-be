package com.pet_care.manager_service.exception;

import com.pet_care.manager_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse resp = new ApiResponse();
        resp.setCode(errorCode.getCode());
        resp.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(resp);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        ApiResponse resp = new ApiResponse();
        resp.setCode(errorCode.getCode());
        resp.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(resp);
    }
}
