package com.petcare.exception;

import com.petcare.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> handleAPIException(APIException e) {

        System.out.println("0");

        APIResponse response = new APIResponse();
        ErrorCode errorCode = e.getErrorCode();

        response.setDate(new Date());
        response.setMessage(errorCode.getMessage());
        response.setStatus(errorCode.getCode());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
