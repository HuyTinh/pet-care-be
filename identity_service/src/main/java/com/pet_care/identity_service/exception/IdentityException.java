package com.pet_care.identity_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentityException extends RuntimeException {
    private ErrorCode errorCode;

    public IdentityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
