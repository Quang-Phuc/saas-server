package com.phuclq.student.types;

public interface BaseEnum {

    public static boolean isEquals(BaseEnum value1, BaseEnum value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.equals(value2);
    }

    public default String getValue() {
        return this.toString().toUpperCase();
    }

}
