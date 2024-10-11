package com.pet_care.appointment_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class APIException extends RuntimeException {
    @NotNull
    private final ErrorCode errorCode;
}
