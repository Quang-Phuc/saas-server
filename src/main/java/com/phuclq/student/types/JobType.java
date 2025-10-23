package com.phuclq.student.types;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum JobType {
    FULL_TIME(1, "Toàn thời gian"), PART_TIME(2, "Bán thời gian");

    public static Map<Integer, String> level = Stream.of(values()).collect(Collectors.toMap(k -> k.code, v -> v.name));
    private final Integer code;
    private final String name;

    JobType(Integer code, String name) {
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
