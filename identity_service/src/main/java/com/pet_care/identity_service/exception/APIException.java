package com.pet_care.identity_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class APIException extends RuntimeException {
    private final ErrorCode errorCode;
}
