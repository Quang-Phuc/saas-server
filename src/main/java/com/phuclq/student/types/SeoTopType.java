package com.phuclq.student.types;


public enum SeoTopType {
    SEO_TOP_FILE(1, "SEO_TOP_FILE"),
    FILE_ZIP(2, "FILE_ZIP"),
    FILE_CONVERT_DOC_PDF(3, "FILE_CONVERT_DOC_PDF"),
    FILE_CUT(4, "FILE_CUT"),
    FILE_DEMO(5, "FILE_DEMO"),
    FILE_UPLOAD(6, "FILE_UPLOAD"),
    USER_AVATAR(7, "USER_AVATAR"),
    JOB_CV_AVATAR(8, "JOB_CV_AVATAR"),
    JOB_AVATAR(9, "JOB_AVATAR"),
    PAYMENT_DONE(10, "PAYMENT_DONE"),
    PAYMENT_QR(11, "PAYMENT_QR"),
    HOME_SHARE(12, "HOME_SHARE"),
    SCHOOL_IMAGE(13, "SCHOOL_IMAGE"),

    HOME_FIND(14, "HOME_FIND");


    private final Integer code;
    private final String name;

    SeoTopType(Integer code, String name) {
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
