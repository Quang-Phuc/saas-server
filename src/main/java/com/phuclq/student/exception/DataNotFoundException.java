package com.phuclq.student.exception;


import com.phuclq.student.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public class DataNotFoundException extends BusinessException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataNotFoundException() {
        super(HttpStatus.NO_CONTENT.toString(), ErrorCode.NO_DATA);
    }

    public DataNotFoundException(String message) {
        super(HttpStatus.NO_CONTENT.toString(), message);
    }

}
