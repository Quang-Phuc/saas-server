package com.phuclq.student.exception;


import com.phuclq.student.constant.ErrorCode;

public class DuplicateEntityException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public DuplicateEntityException() {
        super(ErrorCode.DUPLICATED_ENTITY, ErrorCode.DUPLICATED_ENTITY_DESCRIPTION);
    }

    public DuplicateEntityException(String message) {
        super(ErrorCode.DUPLICATED_ENTITY, message);
    }
}
