package com.pet_care.customer_service.exception;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    CUSTOMER_NOT_FOUND(1001, "Customer not found", HttpStatus.NOT_FOUND),
    PET_NOT_FOUND(1001, "Pet not found", HttpStatus.NOT_FOUND),
    EMAIL_NOT_FOUND(1001, "Email not found", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatus status;
}
