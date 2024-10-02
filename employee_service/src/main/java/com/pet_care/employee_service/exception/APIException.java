package com.pet_care.employee_service.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class APIException extends RuntimeException {
    ErrorCode errorCode;

    public APIException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
