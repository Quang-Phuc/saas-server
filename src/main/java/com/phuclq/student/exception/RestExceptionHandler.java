package com.phuclq.student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(BusinessHandleException.class)
    public ResponseEntity roomExceptionResponse(BusinessHandleException customeException) {
        return new ResponseEntity(customeException.getResponse(), HttpStatus.BAD_REQUEST);
    }
}
