package com.phuclq.student.types;

public enum StatusType {
    ACTIVE(1, "ACTIVE"),
    DONE(2, "DONE");

    private final Integer code;
    private final String name;

    StatusType(Integer code, String name) {
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
