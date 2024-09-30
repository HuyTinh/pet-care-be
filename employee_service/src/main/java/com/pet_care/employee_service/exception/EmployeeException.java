package com.pet_care.employee_service.exception;

public class EmployeeException extends RuntimeException {
    ErrorCode errorCode;

    public EmployeeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
