package com.phuclq.student.exception;

public enum MbalErrorMessage {

    TIMEOUT(MbalErrorMessage.MG00, "timeout"),
    MSG01(MbalErrorMessage.MG01, MbalErrorMessage.MSGMBAL),
    INVALID_PARAM_DOB(MbalErrorMessage.MG01, "invalid-param-dob"),
    INVALID_PARAM_ISSUED_DATE(MbalErrorMessage.MG01, "invalid-param-issued-date"),
    INVALID_PARAM_ID_CARD_NO_CMND(MbalErrorMessage.MG01, "invalid-param-id-card-no-cmnd"),
    INVALID_PARAM_ID_CARD_NO_CCCD(MbalErrorMessage.MG01, "invalid-param-id-card-no-cccd"),
    INVALID_PARAM_ID_CARD_NO_PASPORT(MbalErrorMessage.MG01, "invalid-param-id-card-no-pasport"),
    INVALID_PARAM_ID_CARD_NO_CMQD(MbalErrorMessage.MG01, "invalid-param-id-card-no-cmqd"),
    INVALID_PARAM_FULLNAME(MbalErrorMessage.MG01, "invalid-param-fullname"),
    INVALID_PARAM_GENDER(MbalErrorMessage.MG01, "invalid-param-gender"),
    INVALID_PARAM_ADDRESS(MbalErrorMessage.MG01, "invalid-param-address"),
    INVALID_PARAM_NATIONALITY("MSG02", "invalid-param-nationality"),
    VOUCHER_CODE_USED("MSG04", "voucher-code-used"),
    VOUCHER_CODE_CANNOT_USE("MSG05", "voucher-code-cannot-use"),
    INVALID_PARAM_EMAIL("MSG08", "invalid-param-email"),
    INVALID_PARAM_MOBILE("MSG09", "invalid-param-mobile"),
    MSG10("MSG10", MbalErrorMessage.MSGMBAL),
    INVALID_PARAM_OTP("MSG11", "invalid-param-otp"),
    OTP_TIMEOUT("MSG12", "otp-timeout"),
    INVALID_PARAM_OTP_LENGTH("MSG12", "invalid-param-otp-length"),
    MSG03(MbalErrorMessage.MG03, MbalErrorMessage.MSGMBAL),
    MSG16("MSG16", MbalErrorMessage.MSGMBAL),
    INVALID_PARAM_FILE_TYPE("MSG14", "invalid-param-file-type"),
    INVALID_PARAM_FILE_SIZE("MSG15", "invalid-param-file-size"),
    EXCEPTION_BUSINESS(MbalErrorMessage.MG00, "exception-business"),
    EXCEPTION_AMOUNT_BUSINESS(MbalErrorMessage.MG00, "exception-business"),
    INVALID_PARAM_MBAL("MSGMBAL", MbalErrorMessage.MSGMBAL),
    INVALID_PARAM_MBAL_CONVERT_MSG("MSGMBAL-CONVERT", "msg-mbal-convert"),
    INVALID_PARAM_PACKAGE(MbalErrorMessage.MG03, "invalid-param-package"),
    RATE_LIMIT_EXCEEDED("rate-limit-exceeded", "rate-limit-exceeded"),
    INVALID_AMOUNT_DISCOUNT("MSGMBAL", "invalid-amount-discount"),

    WEBHOOK_OK("0", "success"),
    WEBHOOK_ORDER_NOT_FOUND("400", "order-not-found"),
    WEBHOOK_ORDER_STATUS_INVALID("400", "order-status-invalid"),
    WEBHOOK_ORDER_BUSY("400", "order-busy"),
    WEBHOOK_INVALID_PARAM("400", "invalid-param"),
    WEBHOOK_TOTAL_AMOUNT_PARAM("400", "invalid-total-amount"),
    WEBHOOK_UPDATE_AT_PARAM("400", "invalid-updated-at"),
    WEBHOOK_INVALID_CHECKSUM("400", "invalid-checksum");

    private static final String MG00 = "MSG00";
    private static final String MG01 = "MSG01";
    private static final String MG03 = "MSG03";
    private static final String MSGMBAL = "msg-mbal";

    private String errorCode;
    private String errorMessage;

    private MbalErrorMessage(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
