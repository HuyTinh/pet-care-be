package com.pet_care.medicine_service.exception;

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
    MEDICINE_NOT_FOUND(1001, "Medicine not found", HttpStatus.NOT_FOUND), // Error when a medicine is not found
    CALCULATION_UNIT_NOT_FOUND(1001, "Calculation unit not found", HttpStatus.NOT_FOUND), // Error when a calculation unit is not found
    LOCATION_NOT_FOUND(1001, "Location not found", HttpStatus.NOT_FOUND), // Error when a location is not found
    MANUFACTURE_NOT_FOUND(1001, "Manufacture not found", HttpStatus.NOT_FOUND); // Error when a manufacture is not found

    // Fields for error code, message, and HTTP status
    int code; // The unique error code associated with the error
    String message; // The descriptive message for the error
    HttpStatus status; // The HTTP status to return for this error (e.g., NOT_FOUND)

}
