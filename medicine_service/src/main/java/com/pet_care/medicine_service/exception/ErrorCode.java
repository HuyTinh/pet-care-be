package com.pet_care.medicine_service.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    MEDICINE_NOT_FOUND(1001,"Medicine not found", HttpStatus.NOT_FOUND),;

    int code;
    String message;
    HttpStatus status;
}
