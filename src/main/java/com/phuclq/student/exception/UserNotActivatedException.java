package com.phuclq.student.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

//  public ErpsUserNotActivatedException() {
//    super(ErrorCode.ERROR_USER_NOT_ACTIVE, ErrorCode.ERROR_USER_NOT_ACTIVE_MESSAGE);
//  }

    public UserNotActivatedException(String message) {
        super(message);
    }

}
