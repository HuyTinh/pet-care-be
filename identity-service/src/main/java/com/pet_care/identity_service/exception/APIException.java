package com.pet_care.identity_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class APIException extends RuntimeException {
    
    private final ErrorCode errorCode;
}
