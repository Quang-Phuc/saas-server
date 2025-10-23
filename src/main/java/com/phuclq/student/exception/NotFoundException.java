package com.phuclq.student.exception;


import com.phuclq.student.constant.ErrorCode;

public class NotFoundException extends BusinessException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND_ENTITY, ErrorCode.NOT_FOUND_ENTITY_DESCRIPTION);
    }

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND_ENTITY, message);
    }

}
