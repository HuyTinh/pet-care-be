package com.pet_care.medicine_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    MEDICINE_NOT_FOUND(1001, "Medicine not found", HttpStatus.NOT_FOUND),
    CALCULATION_UNIT_NOT_FOUND(1001, "Calculation unit not found", HttpStatus.NOT_FOUND),
    LOCATION_NOT_FOUND(1001, "Location not found", HttpStatus.NOT_FOUND),
    MANUFACTURE_NOT_FOUND(1001, "Manufacture not found", HttpStatus.NOT_FOUND),
    ;

    int code;
    @NotNull String message;
    @NotNull HttpStatus status;
}
