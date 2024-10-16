package com.petcare.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIException extends RuntimeException {

    ErrorCode errorCode;

    public APIException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
