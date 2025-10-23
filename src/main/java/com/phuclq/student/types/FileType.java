package com.phuclq.student.types;


public enum FileType {
    FILE_AVATAR(1, "FILE_AVATAR"),
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
    HOME_FIND(14, "HOME_FIND"),
    FILE_BANNER(15, "FILE_BANNER"),
    FILE_BLOG(16, "FILE_BLOG"),
    SELL_IMAGE(17, "SELL_IMAGE"),
    BLOG_IMAGE(18, "BLOG_IMAGE"),
    SALE_IMAGE(19,"SALE_IMAGE" );


    private final Integer code;
    private final String name;

    FileType(Integer code, String name) {
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
