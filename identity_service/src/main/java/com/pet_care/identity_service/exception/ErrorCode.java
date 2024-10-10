package com.pet_care.identity_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002, "Account exist", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at less 5 characters", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_INVALID(1003, "Phone number must be at least 3 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1003, "Email isn't valid", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(1001, "Account isn't exist", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_CORRECT(1003, "Password isn't correct please check again", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You don't have permission", HttpStatus.FORBIDDEN),
    ROLE_NOT_EXISTED(1001, "Role isn't exist", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1002, "Role exist", HttpStatus.BAD_REQUEST),
    TOKEN_HAS_EXPIRED(1011, "Token has expired", HttpStatus.UNAUTHORIZED),
    PERMISSION_NOT_FOUND(1001, "Permission not found", HttpStatus.NOT_FOUND);

    int code;
    String message;
    HttpStatus status;
}
