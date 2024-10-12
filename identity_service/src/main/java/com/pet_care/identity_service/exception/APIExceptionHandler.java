package com.pet_care.identity_service.exception;

import com.pet_care.identity_service.dto.response.APIResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@ControllerAdvice
public class APIExceptionHandler {

    /**
     * @param ex
     * @return
     */
    @NotNull
    @ExceptionHandler(Exception.class)
    ResponseEntity<APIResponse<?>> handlingRuntimeException(@NotNull RuntimeException ex) {
        ErrorCode errorCode = ErrorCode.valueOf(ex.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(APIResponse.builder().code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()).message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage()).build());
    }

    /**
     * @param ex
     * @return
     */
    @NotNull
    @ExceptionHandler(APIException.class)
    ResponseEntity<APIResponse<?>> handlingIdentityException(@NotNull APIException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(APIResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }

    @NotNull
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<APIResponse<?>> handlingAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getCode()).body(APIResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }

    /**
     * @param ex
     * @return
     */
    @NotNull
    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<APIResponse<?>> handlingAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatus()).body(APIResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }

    /**
     * @param ex
     * @return
     */
    @NotNull
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<?>> handlingMethodArgumentNotValidException(@NotNull MethodArgumentNotValidException ex) {
        String enumKey = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ignored) {
        }

        return ResponseEntity.badRequest().body(APIResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }
}
