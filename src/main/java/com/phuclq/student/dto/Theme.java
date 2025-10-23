package com.phuclq.student.dto;

public enum Theme {
    LIGHT("light");

    private String name;

    Theme(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
