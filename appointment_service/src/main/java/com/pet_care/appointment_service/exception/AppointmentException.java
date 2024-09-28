package com.pet_care.appointment_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentException extends RuntimeException {
    private ErrorCode errorCode;

    public AppointmentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }
}
