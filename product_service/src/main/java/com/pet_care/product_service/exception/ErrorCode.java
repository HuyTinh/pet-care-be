package com.pet_care.product_service.exception;

// Importing necessary libraries and annotations
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

// Enum to define different error codes related to medicine service
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    // Enum values for different error scenarios in the application
    PRODUCT_NOT_FOUND(1001, "Product not found", HttpStatus.NOT_FOUND), // Error when a medicine is not found
    CATEGORY_EXIST(1002, "Product name is exist", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1001, "Category unit not found", HttpStatus.NOT_FOUND), // Error when a calculation unit is not found
    ;

    // Fields for error code, message, and HTTP status
    int code; // The unique error code associated with the error
    String message; // The descriptive message for the error
    HttpStatus status; // The HTTP status to return for this error (e.g., NOT_FOUND)

}
