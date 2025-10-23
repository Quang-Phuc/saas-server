package com.phuclq.student.exception;

import org.springframework.security.core.AuthenticationException;

public class NotFoundUserNameException extends AuthenticationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

//  private String status;
//  public ErpsNotFoundUserNameException() {
//    super(ErrorCode.ERROR_NOT_FOUND_USER_NAME, ErrorCode.ERROR_NOT_FOUND_USER_NAME_MESSAGE);
//  }

    public NotFoundUserNameException(String message) {
        super(message);
//    this.status = ErrorCode.ERROR_NOT_FOUND_USER_NAME;
    }

}
