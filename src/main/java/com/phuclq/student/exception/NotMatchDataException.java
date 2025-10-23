package com.phuclq.student.exception;


import com.phuclq.student.constant.ErrorCode;

public class NotMatchDataException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public NotMatchDataException() {
        super(ErrorCode.NOT_MATCH_DATA, ErrorCode.NOT_MATCH_DATA_DESCRIPTION);
    }

    public NotMatchDataException(String message) {
        super(ErrorCode.NOT_MATCH_DATA, message);
    }

}
