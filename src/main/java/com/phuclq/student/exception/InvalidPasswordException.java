package com.phuclq.student.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidPasswordException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super("Incorrect password");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
