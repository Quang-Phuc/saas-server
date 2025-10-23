package com.phuclq.student.exception;


import com.phuclq.student.constant.ErrorCode;

public class InvalidInputRequestException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public InvalidInputRequestException() {
        super(ErrorCode.INVALID_DATA_REQUEST, ErrorCode.INVALID_DATA_REQUEST_DESCRIPTION);
    }

    public InvalidInputRequestException(String message) {
        super(ErrorCode.INVALID_DATA_REQUEST, message);
    }
}
