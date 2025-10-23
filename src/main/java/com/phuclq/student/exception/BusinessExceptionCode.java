package com.phuclq.student.exception;

public class BusinessExceptionCode {

    // BASE
    public static final String BAD_CREDENTIAL = "bad-credential";
    public static final String NEED_CAPTCHA = "need-captcha";
    public static final String CAPTCHA_INVALID = "captcha-invalid";
    public static final String ACCESS_DENIED = "access-denied";
    public static final String INVALID_PARAM = "invalid-param";
    public static final String INVALID_EXCEL = "invalid-excel";
    public static final String INVALID_USERNAME = "invalid-username";
    public static final String INVALID_FULLNAME = "invalid-fullname";
    public static final String INVALID_EMAIL = "invalid-email";
    public static final String INVALID_PHONE = "invalid-phone";
    public static final String INVALID_DOB = "invalid-dob";
    public static final String INVALID_LINK = "invalid-link";
    public static final String INVALID_OTP = "invalid-otp";
    public static final String FILE_SIZE_LIMIT_EXCEEDED = "file-size-limit-exceeded";
    public static final String UNSUPPORTED_IMAGE = "unsupported-image";
    public static final String INVALID_IMAGE = "invalid-image";
    public static final String INVALID_IMAGE_BANNER = "invalid-image-banner";
    public static final String INVALID_IMAGE_PRODUCT = "invalid-image-product";
    public static final String INVALID_IMAGE_EVENT_NEW = "invalid-image-event-new";
    public static final String INVALID_IMAGE_PROMOTION = "invalid-image-promotion";
    public static final String INVALID_IMAGE_PRODUCT_ONLINE = "invalid-image-product-online";
    public static final String INVALID_IMAGE_PRODUCT_ONLINE_HOME = "invalid-image-product-online-home";
    public static final String INVALID_IMAGE_PRODUCT_ONLINE_DETAIL = "invalid-image-product-online-detail";
    public static final String INVALID_IMAGE_FILE_SIZE = "invalid-image-file-size";
    public static final String INVALID_IMAGE_RESOLUTION = "invalid-image-size";
    public static final String INVALID_OLD_PASSWORD = "invalid-old-password";
    public static final String INVALID_NEW_PASSWORD = "invalid-new-password";
    public static final String INVALID_STRONG_PASSWORD = "invalid-strong-password";
    public static final String RECORD_CHANGED_PLEASE_REFRESH = "record-changed-please-refresh";
    public static final String USERNAME_USED = "username-used";
    public static final String NAME_USED = "name-used";
    public static final String NAME_USED_BANNER = "name-used-banner";
    public static final String NAME_USED_EVENT_NEW = "name-used-event-new";
    public static final String NAME_USED_PRODUCT = "name-used-product";
    public static final String NAME_USED_PROMOTION = "name-used-promotion";
    public static final String CODE_USED = "code-used";
    public static final String RECORD_IS_USING = "record-is-using";
    public static final String TODATE_BEFORE_FROMDATE = "todate-before-fromdate";
    public static final String TODATE_BEFORE_FROMDATE_PROMOTION = "todate-before-fromdate-promotion";
    public static final String TODATE_BEFORE_CURRENT_TIME_PROMOTION = "todate-before-current-time-promotion";
    public static final String FROMDATE_BEFORE_CURRENT_TIME_PROMOTION = "fromdate-before-fromdate-current-time-promotion";
    public static final String PERIOD_MORE_THAN_7_DAYS = "period-more-than-7-days";
    public static final String INVALID_POST_TIME = "invalid-post-time";
    // BUSINESS
    public static final String ORDER_NOT_FOUND = "order-not-found";
    public static final String ORDER_STATUS_INVALID = "order-status-invalid";
    public static final String ORDER_BUSY = "order-busy";
    public static final String CATEGORY_CHANGED = "category-changed";
    public static final String TOKEN_BUSY = "token-busy";
    public static final String TOKEN_HAS_ORDER_PAID = "token-has-order-paid";
    public static final String INVALID_CHECKSUM = "invalid-checksum";
    public static final String INVALID_DATE = "invalid-date";
    public static final String CALL_SERVER_TIMEOUT = "call-server-timeout";
    public static final String INVALID_REQUEST = "invalid-request";
    public static final String CALL_BACK_MBAL_FAIL = "callback-mbal-fail";
    public static final String VERIFICATION_MAX_SEND_OTP = "verification-max-send-otp";
    public static final String VERIFICATION_MAX_RESEND_OTP = "verification-max-resend-otp";
    public static final String VERIFICATION_MAX_VERIFY_OTP = "verification-max-verify-otp";
    public static final String INVALID_VERIFICATION_REQUEST = "invalid-verification-request";
    public static final String TIMEOUT_OTP = "timeout-otp";
    public static final String INVALID_REQUEST_TOKEN = "invalid-request-token";
    public static final String INVALID_TOKEN_SECRET = "invalid-token-secret";
    public static final String INVALID_MERCHANT_TOKEN = "invalid-merchant-token";
    public static final String INVALID_MERCHANT_CODE = "invalid-merchant-code";
    public static final String INVALID_SELLER_INFO = "invalid-seller-info";
    public static final String ORDER_PAYMENT_TYPE_INVALID = "order-payment-type-invalid";
    private BusinessExceptionCode() {
    }

}
