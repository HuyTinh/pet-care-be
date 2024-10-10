package com.pet_care.bill_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    INVOICE_NOT_FOUND(1001, "Invoice not found", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatus status;
}
