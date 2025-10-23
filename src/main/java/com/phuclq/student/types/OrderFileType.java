package com.phuclq.student.types;


public enum OrderFileType {
    PRICE(1, "PRICE"), DOWNLOADS(2, "DOWNLOADS"), VIEW(3, "VIEW"), FAVORITES(4, "FAVORITES");


    private final Integer code;
    private final String name;

    OrderFileType(Integer code, String name) {
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
