package com.petcare.exception;

import lombok.Getter;
import lombok.Setter;

//@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
public class APIException extends RuntimeException{

    ErrorCode errorCode;

    public APIException(ErrorCode errorCode) {
        this.errorCode = errorCode;
//        super(message);
    }
}
