package com.pet_care.customer_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerException extends RuntimeException {
    private ErrorCode errorCode;

    public CustomerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }
}
