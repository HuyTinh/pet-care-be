package com.pet_care.employee_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class APIException extends RuntimeException {
    ErrorCode errorCode;
}
