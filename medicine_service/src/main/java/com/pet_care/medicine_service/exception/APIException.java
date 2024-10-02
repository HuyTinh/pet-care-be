package com.pet_care.medicine_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIException extends RuntimeException {
    ErrorCode errorCode;
}
