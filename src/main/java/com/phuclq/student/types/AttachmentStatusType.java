package com.phuclq.student.types;

public enum AttachmentStatusType {
    ACTIVE(1, "ACTIVE");

    private final Integer code;
    private final String name;

    AttachmentStatusType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }
}
