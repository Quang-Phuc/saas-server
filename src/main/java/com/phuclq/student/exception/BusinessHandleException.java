package com.phuclq.student.exception;


import org.springframework.beans.factory.annotation.Autowired;

public class BusinessHandleException extends RuntimeException {

    @Autowired
    private ErrorMessageLoader errorMessageLoader;
    private ExceptionResponse response;

    public BusinessHandleException(String code) {
        super(code);
        this.response = new ExceptionResponse(code, errorMessageLoader.getMessage(code));
    }

    public ExceptionResponse getResponse() {
        return response;
    }
}
