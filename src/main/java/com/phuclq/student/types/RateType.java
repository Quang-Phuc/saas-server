package com.phuclq.student.types;


public enum RateType {
    RATE_FILE(1, "RATE_FILE"),
    RATE_SCHOOL(2, "RATE_SCHOOL");


    private final Integer code;
    private final String name;

    RateType(Integer code, String name) {
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
