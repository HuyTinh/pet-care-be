package com.petcare.exception;

import com.petcare.dto.response.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class APIExceptionHandler
{

    @ExceptionHandler(APIException.class)
    public ResponseEntity<DataResponse> handleAPIException(APIException ex) {

        ErrorCode errorCode = ex.getErrorCode();
        DataResponse data = new DataResponse();
        data.setMessage(errorCode.getMessage());
        data.setStatus(errorCode.getCode());
        data.setDateTime(LocalDate.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);

    }

}
