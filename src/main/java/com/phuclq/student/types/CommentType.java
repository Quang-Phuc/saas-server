package com.phuclq.student.types;


public enum CommentType {
    COMMENT_FILE(1, "COMMENT_FILE"),
    COMMENT_SCHOOL(2, "COMMENT_SCHOOL");


    private final Integer code;
    private final String name;

    CommentType(Integer code, String name) {
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
