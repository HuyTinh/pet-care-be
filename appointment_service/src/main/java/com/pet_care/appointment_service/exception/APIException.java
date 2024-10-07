package com.pet_care.appointment_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class APIException extends RuntimeException {
    private final ErrorCode errorCode;
}
