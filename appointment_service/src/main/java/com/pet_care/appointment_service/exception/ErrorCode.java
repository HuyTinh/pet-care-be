package com.pet_care.appointment_service.exception;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    APPOINTMENT_NOT_FOUND(1001, "Appointment not found", HttpStatus.NOT_FOUND),
    HOSPITAL_SERVICE_NOT_FOUND(1001, "Service not found", HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND(1001, "Customer not found", HttpStatus.NOT_FOUND),
    PET_NOT_FOUND(1001, "Pet not found", HttpStatus.NOT_FOUND),
    EMAIL_NOT_FOUND(1001, "Email not found", HttpStatus.NOT_FOUND),
    SPECIE_NOT_FOUND(1001, "Specie not found", HttpStatus.NOT_FOUND),
    ;
    int code;
     String message;
     HttpStatusCode status;
}
