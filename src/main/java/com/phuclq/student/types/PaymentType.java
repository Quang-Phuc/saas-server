package com.phuclq.student.types;

public enum PaymentType {
    PAYMENT_PROCESSING(1, "PAYMENT_PROCESSING"),
    PAYMENT_DONE(2, "PAYMENT_DONE"),
    PAYMENT_REJECT(3, "PAYMENT_REJECT");

    private final Integer code;
    private final String name;

    PaymentType(Integer code, String name) {
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
