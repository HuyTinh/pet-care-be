package com.pet_care.medical_prescription_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    PRESCRIPTION_NOT_FOUND(1001, "Prescription not found", HttpStatus.NOT_FOUND),
    VETERINARY_CARE_NOT_FOUND(1001, "Veterinary Care not found", HttpStatus.NOT_FOUND);

    int code;
    String message;
    HttpStatus status;
}
