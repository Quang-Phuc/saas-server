package com.phuclq.student.exception;


import com.phuclq.student.constant.ErrorCode;

public class FailToExecuteException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public FailToExecuteException() {
        super(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
    }

    public FailToExecuteException(String message) {
        super(ErrorCode.FAILED_TO_EXECUTE, message);
    }

}
