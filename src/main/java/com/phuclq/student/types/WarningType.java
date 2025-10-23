package com.phuclq.student.types;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WarningType {
    COIN_COMPARE_USER(1, "COIN_COMPARE_USER");

    public static Map<Integer, String> level = Stream.of(values())
            .collect(Collectors.toMap(k -> k.code, v -> v.name));
    private final Integer code;
    private final String name;

    WarningType(Integer code, String name) {
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
