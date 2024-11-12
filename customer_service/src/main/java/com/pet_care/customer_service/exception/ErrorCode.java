package com.pet_care.customer_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * Enum to define error codes, messages, and HTTP statuses for different types of errors in the application.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    // Error code for when a customer is not found in the system
    CUSTOMER_NOT_FOUND(1001, "Customer not found", HttpStatus.NOT_FOUND),

    // Error code for when a pet is not found in the system
    PET_NOT_FOUND(1001, "Pet not found", HttpStatus.NOT_FOUND),

    // Error code for when an email is not found in the system
    EMAIL_NOT_FOUND(1001, "Email not found", HttpStatus.NOT_FOUND);

    // Unique code associated with the error
    int code;

    // Message describing the error
    String message;

    // HTTP status associated with the error
    HttpStatus status;
}
