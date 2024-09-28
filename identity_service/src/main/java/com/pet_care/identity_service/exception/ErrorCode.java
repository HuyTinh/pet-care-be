package com.pet_care.identity_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "Account exist", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at less 5 characters", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_INVALID(1013, "Phone number must be at least 3 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1005, "Email isn't valid", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(1006, "Account isn't exist", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_CORRECT(1013,"Password isn't correct", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You don't have permission", HttpStatus.FORBIDDEN),
    ROLE_NOT_EXISTED(1009, "Role isn't exist", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1010, "Role exist", HttpStatus.BAD_REQUEST),
    TOKEN_HAS_EXPIRED(1011, "Token has expired", HttpStatus.UNAUTHORIZED),
    PERMISSION_NOT_FOUND(1012, "Permission not found", HttpStatus.NOT_FOUND);

    int code;
    String message;
    HttpStatusCode statusCode;
}
