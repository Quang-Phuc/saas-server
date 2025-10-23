package com.phuclq.student.types;


public enum ReportType {
    REPORT_FILE(1, "REPORT_FILE");


    private final Integer code;
    private final String name;

    ReportType(Integer code, String name) {
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
