package com.pet_care.identity_service.exception;

import com.pet_care.identity_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    ApiResponse handlingRuntimeException(RuntimeException ex) {
        return ApiResponse.builder().code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()).message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage()).build();
    }


    @ExceptionHandler(IdentityException.class)
    ApiResponse handlingIdentityException(IdentityException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ApiResponse handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ApiResponse.builder().code(1002).message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()).build();
    }
}
