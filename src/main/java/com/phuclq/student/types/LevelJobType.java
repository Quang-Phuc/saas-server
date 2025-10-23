package com.phuclq.student.types;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LevelJobType {
    UNIVERSITY(1, "Đại học"), COLLEGE(2, "Cao đẳng"), HIGH_SCHOOL(3, "Trung học phổ thông"), COMMON(4,
            "Trung học cơ sở");

    public static Map<Integer, String> level = Stream.of(values())
            .collect(Collectors.toMap(k -> k.code, v -> v.name));
    private final Integer code;
    private final String name;

    LevelJobType(Integer code, String name) {
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
