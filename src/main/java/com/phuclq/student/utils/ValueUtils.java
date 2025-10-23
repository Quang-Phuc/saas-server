package com.phuclq.student.utils;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class ValueUtils {
    public static final Integer DEFAULT_AGENT_INT = -1;
    public static final Integer DEFAULT_OTP_MINUTES_NUMBER = 5;
    public static final Integer DEFAULT_INT = 0;
    public static final String DEFAULT_STR = "";
    public static final BigDecimal DEFAULT_DECIMAL = BigDecimal.ZERO;
    public static final Integer NUMBER_TWO = 2;

    public static Integer getOtpMinMinutesExpire(String value) {
        return value == null ? DEFAULT_OTP_MINUTES_NUMBER : toDefaultInt(value);
    }

    public static Integer getOtpMinMinutesExpire() {
        return DEFAULT_OTP_MINUTES_NUMBER;
    }

    public static Integer toDefaultInt(Integer value) {
        return value == null ? DEFAULT_INT : value;
    }

    public static Integer toDefaultInt(String value) {
        return value == null ? 0 : Integer.parseInt(value);
    }

    public static String toString(Integer value) {
        return value == null ? "" : value.toString();
    }

    public static ZonedDateTime toLocalDateTimeNow() {
        return ZonedDateTime.now();
    }

}
