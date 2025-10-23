package com.phuclq.student.constant;

public class ErrorCode {

    public static final String UNKNOWN_ERROR = "01";
    public static final String UNKNOWN_ERROR_DESCRIPTION = "Unknown error";

    public static final String SUCCESS = "02";
    public static final String SUCCESS_DESCRIPTION = "Successful";

    public static final String INVALID_DATA_REQUEST = "03";
    public static final String INVALID_DATA_REQUEST_DESCRIPTION = "Invalid input's request";

    public static final String NOT_FOUND_ENTITY = "04";
    public static final String NOT_FOUND_ENTITY_DESCRIPTION = "Not found entity";

    public static final String DUPLICATED_ENTITY = "05";
    public static final String DUPLICATED_ENTITY_DESCRIPTION = "Duplicated entity";

    public static final String NOT_MATCH_DATA = "06";
    public static final String NOT_MATCH_DATA_DESCRIPTION = "Not match data";

    public static final String UNAUTHORIZED = "07";
    public static final String UNAUTHORIZED_DESCRIPTION = "No permission to do";

    public static final String INVALID_SESSION = "08";
    public static final String INVALID_SESSION_DESCRIPTION = "Invalid session";

    public static final String FAILED_TO_EXECUTE = "09";
    public static final String FAILED_TO_EXECUTE_DESCRIPTION = "Failed to execute";

    public static final String CONNECTION_TIMEOUT = "10";
    public static final String CONNECTION_TIMEOUT_DESCRIPTION = "Connection timeout";

    public static final String TOKEN_EXPIRED = "11";
    public static final String TOKEN_EXPIRED_DESCRIPTION = "Token expired";

    public static final String ENTITY_NAME_EMPTY_OR_NULL = "12";
    public static final String ENTITY_NAME_EMPTY_OR_NULL_DESCRIPTION = "Entity name is empty";

    public static final String ERROR_NOT_RECEIVE_MESSAGE = "13";

    public static final String ERROR_LIMIT_REV = "14";
    public static final String ERROR_LIMIT_REV_MESSAGE = "All record greater than maximum";

    public static final String ERROR_ACCOUT_RESOURCE = "15";
    public static final String ERROR_ACCOUT_RESOURCE_MESSAGE = "Agent Authenticate error";

    public static final String ERROR_INVALID_PASSWORD = "16";
    public static final String ERROR_INVALID_PASSWORD_MESSAGE = "Agent invalid pasword";

    public static final String ERROR_NOT_FOUND_USER_NAME = "17";
    public static final String ERROR_NOT_FOUND_USER_NAME_MESSAGE = "Not found username";

    public static final String ERROR_USER_NOT_ACTIVE = "18";
    public static final String ERROR_USER_NOT_ACTIVE_MESSAGE = "User not actived";

    public static final String ERROR_NOT_ENOUGH_COIN = "19";
    public static final String ERROR_NOT_ENOUGH_COIN_MESSAGE = "You need to top up";

    public static final String ERROR_FILE_EXISTS = "20";
    public static final String ERROR_FILE_EXISTS_MESSAGE = "File is exists";

    public static final String NO_DATA = "No data";

    public static final String ERROR_USER_PASSWORD_NOT_MATCH = "40";

    public static final String ERROR_API_EXIST = "404";


    public static final String ERROR_EMAIL_EXISTS = "Địa chỉ email đã được đăng ký";
    public static final String ERROR_CAPTCHA_FAIL = "Captcha không chính xác";
}
