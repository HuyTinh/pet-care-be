package com.pet_care.employee_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

/**
 * Enum that defines various error codes and associated messages.
 * This enum is used for consistent error handling across the application.
 * Each error code is associated with a unique error code, message, and HTTP status.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    // Enum constants for specific error scenarios
    EMPLOYEE_NOT_FOUND(1001, "Employee not found", HttpStatus.NOT_FOUND),  // Error when an employee is not found
    EMAIL_EXIST(1002, "Email already exists", HttpStatus.CONFLICT);  // Error when an email already exists

    int code;          // Unique error code to identify the specific error
    String message;    // Message describing the error
    HttpStatus status; // HTTP status associated with the error

    /**
     * Retrieves the error code.
     * @return the unique error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Retrieves the error message.
     * @return the message associated with the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieves the HTTP status associated with the error.
     * @return the HTTP status
     */
    public HttpStatus getStatus() {
        return status;
    }
}
