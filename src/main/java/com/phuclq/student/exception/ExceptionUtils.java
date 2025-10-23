package com.phuclq.student.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {

    public static final String E_INTERNAL_SERVER = "E_INTERNAL_SERVER";
    public static final String E_COMMON_DUPLICATE_CODE = "E_COMMON_DUPLICATE_CODE";
    public static final String E_COMMON_NOT_EXISTS_CODE = "E_COMMON_NOT_EXISTS_CODE";
    public static final String E_NOT_MATCH_CODE = "E_NOT_MATCH_CODE";
    public static final String COMMON_NULL_STRING = "COMMON_NULL_STRING";
    public static final String COMMON_CATEGORY_DUPLICATE_CODE = "COMMON_CATEGORY_DUPLICATE_CODE";
    public static final String COMMON_DUPLICATE_CODE = "COMMON_DUPLICATE_CODE";
    public static final String COMMON_ONLY_ONE_DEFAULT = "COMMON_ONLY_ONE_DEFAULT";
    public static final String COMMON_LESS_THAN_0 = "COMMON_LESS_THAN_0";
    public static final String COMMON_POSITION = "COMMON_POSITION";
    public static final String COMMON_EXIST = "COMMON_EXIST";
    public static final String COMMON_DELETED = "COMMON_DELETED";
    public static final String E_EXPORT_001 = "E_EXPORT_001";
    public static final String ABILITY_NULL_STRING = "ABILITY_NULL_STRING";
    public static final String ABILITY_EXIST = "ABILITY_EXIST";
    public static final String ABILITY_NOT_EXIST = "ABILITY_NOT_EXIST";
    public static final String ABILITY_LEVEL = "ABILITY_LEVEL";
    public static final String ABILITY_EXIST_GROUP = "ABILITY_EXIST_GROUP";
    public static final String ABILITY_EXIST_CLASSIFY = "ABILITY_EXIST_CLASSIFY";
    public static final String ABILITY_INVALID_SIZE = "ABILITY_INVALID_SIZE";
    public static final String ABILITY_NULL_LEVEL_REQUIRED = "ABILITY_NULL_LEVEL_REQUIRED";
    public static final String LEVEL_UNIQUE_CODE = "LEVEL_UNIQUE_CODE";
    public static final String E_BODY_INFO_MISSING = "E_BODY_INFO_MISSING";
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
    public static final String PROCESS_ALREADY_END = "PROCESS_ALREADY_END";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String INVALID_POLICY_NUMBER = "INVALID_POLICY_NUMBER";
    public static final String SIGNING_DATE_CAN_NOT_OVER_21_DAY_FROM_ACK_DATE = "GREATER_THAN_21_DAY";
    public static final String SAP_NOT_RESPONSE = "SAP_NOT_RESPONSE";
    public static final String REQUEST_TYPE_NOT_EXIST = "REQUEST_TYPE_NOT_EXIST";
    public static final String NUM_OF_FILES_AND_TYPES_NOT_EQUAL = "NUM_OF_FILES_AND_TYPES_NOT_EQUAL";
    public static final String INVALID_FILE_TYPES_STRING_REQUEST =
            "INVALID_FILE_TYPES_STRING_REQUEST";
    public static final String REQUEST_NOT_EXIST = "SS021";
    public static final String NOT_YET_DECISION = "DECISION_HAS_NOT_YET_BEEN_MADE";
    public static final String INVALID_FILE_TYPE_UPLOAD = "INVALID_FILE_TYPE_UPLOAD";
    public static final String MISSING_REQUIRED_FILE = "MISSING_REQUIRED_FILE";
    public static final String MAX_FILE_SIZE_UPLOAD = "MAX_FILE_SIZE_UPLOAD";
    public static final String REVERSAL_FAIL = "REVERSAL_FAIL";
    public static final String SEARCH_PARAM_VALUE_NOT_MATCH_ANY_ENUM_VALUE =
            "SEARCH_PARAM_VALUE_NOT_MATCH_ANY_ENUM_VALUE";
    public static final String USER_NOT_EXIST = "USER_NOT_EXIST";
    public static final String INVALID_POLICY_NUMBER_NO_FLP_DATE =
            "INVALID_POLICY_NUMBER_NO_FLP_DATE";
    public static final String INVALID_POLICY_NUMBER_NO_POLICY_NUMBER_RETURN =
            "INVALID_POLICY_NUMBER_NO_POLICY_NUMBER_RETURN";
    public static final String POLICY_NUMBER_REQUEST_EXIST =
            "POLICY_NUMBER_ALREADY_HAS_REQUEST_IN_PROCESS";
    public static final String TOKEN_EXPIRES = "TOKEN_EXPIRES";
    public static final String CAN_NOT_FORWARD_TO_YOURSELF = "CAN_NOT_FORWARD_TO_YOURSELF";
    public static final String CAN_NOT_FORWARD = "CAN_NOT_FORWARD";
    public static final String ATTACHMENT_NOT_EXIST = "ATTACHMENT_NOT_EXIST";
    public static final String EXPORT_REPORT_FAIL = "EXPORT_REPORT_FAIL";
    public static final String CREATE_PAYMENT_REQUEST_FAIL = "CREATE_PAYMENT_REQUEST_FAIL";
    public static final String NOT_CHOSEN_OR_ADD_NEW_BANK_ACCOUNT_YET =
            "NOT_CHOSEN_OR_ADD_NEW_BANK_ACCOUNT_YET";
    public static final String CREATE_PAYMENT_RUN_FAIL = "CREATE_PAYMENT_RUN_FAIL";
    public static final String UPLOAD_ATTACHMENT_TO_SAP_FAIL = "UPLOAD_ATTACHMENT_TO_SAP_FAIL";
    public static final String NO_SEARCH_RESULT_FOUND = "NO_SEARCH_RESULT_FOUND";
    public static final String BANK_ACCOUNT_HAD_EXIST = "BANK_ACCOUNT_HAD_EXIST";
    public static final String MEDICAL_FEE_CANNOT_GREATER_THAN_POA_AND_PREMIUM_AMOUNT =
            "MEDICAL_FEE_CANNOT_GREATER_THAN_POA_AND_PREMIUM_AMOUNT";
    public static final String FILTER_CAN_NOT_NULL = "FILTER_CAN_NOT_NULL";
    public static final String USED_TO_PAYMENT_RUN = "USED_TO_PAYMENT_RUN";
    public static final String CREATE_PAYMENT_RUN_FAIL_RESPONSE_LIST_DOC_NULL =
            "CREATE_PAYMENT_RUN_FAIL_RESPONSE_LIST_DOC_NULL";
    public static final String PAYMENT_RUN_RESPONSE_DO_NOT_CONTAIN_DOC_11 =
            "PAYMENT_RUN_RESPONSE_DO_NOT_CONTAIN_DOC_11";
    public static final String INVALID_INPUT_BANK_ACCOUNT_NUMBER =
            "INVALID_INPUT_BANK_ACCOUNT_NUMBER";
    public static final String BANK_ACCOUNT_DOES_NOT_EXIST_OP_SAP =
            "BANK_ACCOUNT_DOES_NOT_EXIST_OP_SAP";
    public static final String APPLY_NEW_RULE_FAIL = "APPLY_NEW_RULE_FAIL";
    public static final String BPM_COMPLETE_TASK_WITH_NO_VARIABLES =
            "BPM_COMPLETE_TASK_WITH_NO_VARIABLES";
    public static final String TEST_MULTI_LANG_MESSAGE = "TEST_MULTI_LANG_MESSAGE";
    public static final String ADD_NEW_BANK_FAIL = "ADD_NEW_BANK_FAIL";
    public static final String NOT_PDF = "FILE_IS_NOT_PDF";
    public static final String FILE_IS_EMPTY = "FILE_IS_EMPTY";
    public static final String DECISION_MUST_BE_APPROVED_BEFORE_PROCESSING =
            "DECISION_MUST_BE_APPROVED_BEFORE_PROCESSING";
    public static final String REQUEST_IS_NOT_REQUIRED_ADDITIONAL_INFORMATION =
            "REQUEST_IS_NOT_REQUIRED_ADDITIONAL_INFORMATION";
    public static final String PAYMENT_LOT_FAIL = "PAYMENT_LOT_FAIL";
    public static final String INVALID_VALUE_VALIDATED = "INVALID_VALUE_VALIDATED";
    public static final String ASSIGN_REQUEST_FAIL = "ASSIGN_REQUEST_FAIL";
    public static final String WRONG_REQ_TYPE = "WRONG_REQ_TYPE";
    public static final String BANK_CODE_NOT_FOUND = "BANK_CODE_NOT_FOUND";
    public static final String APPLICATION_NUMBER_NOT_FOUND = "APPLICATION_NUMBER_NOT_FOUND";
    public static final String TASK_NOT_EXIST = "TASK_NOT_EXIST";
    public static final String MISSING_DECISION_REASON = "MISSING_DECISION_REASON";
    public static final String BUSINESS_PARTNER_NOT_EXIST = "BUSINESS_PARTNER_NOT_EXIST";
    public static final String THIS_APPLICATION_NUMBER_IS_ALREADY_PUBLISHED =
            "THIS_APPLICATION_NUMBER_IS_ALREADY_PUBLISHED";
    public static final String THIS_POLICY_NUMBER_IS_NOT_YET_PUBLISHED =
            "THIS_POLICY_NUMBER_IS_NOT_YET_PUBLISHED";
    public static final String THIS_POLICY_NUMBER_STATUS_IS_DORMANT_OR_LAPSE =
            "THIS_POLICY_NUMBER_STATUS_IS_DORMANT_OR_LAPSE";
    public static final String NO_APPROVED_RETURN = "NO_APPROVED_RETURN";
    public static final String PAYMENT_METHOD_NOT_VALID = "PAYMENT_METHOD_NOT_VALID";
    public static final String ADD_DEFAULT_BANK_FAIL_WHEN_REFUND_CASH =
            "ADD_DEFAULT_BANK_FAIL_WHEN_REFUND_CASH";
    public static final String REFUND_BANK_ACCOUNT_DOES_NOT_EXIST_ON_SAP_BP =
            "REFUND_BANK_ACCOUNT_DOES_NOT_EXIST_ON_SAP_BP";
    public static final String SAP_500 = "SAP_INTERNAL_ERROR";
    public static final String APPROVE_BUT_NO_CANCELLATION_FORM = "APPROVE_BUT_NO_CANCELLATION_FORM";
    public static final String POLICY_NUMBER_NOT_REVESAL_YET = "POLICY_NUMBER_NOT_REVESAL_YET";
    public static final String EMPTY_PAYMENT_NUMBER = "EMPTY_PAYMENT_NUMBER";
    public static final String NOT_PICK_ANY_LOT = "NOT_PICK_ANY_PAYMENT_LOT";
    public static final String SUBMIT_APPROVED_REQ_BUT_NOT_YET_PROCESSED =
            "SUBMIT_APPROVED_REQ_BUT_NOT_YET_PROCESSED";
    public static final String THIS_APPLICATION_NUMBER_ALREADY_HAVE_REQUEST_IN_PROCESS =
            "THIS_APPLICATION_NUMBER_ALREADY_HAVE_REQUEST_IN_PROCESS";
    public static final Map<String, String> messages;
    public static final String NOT_RIGHT_STEP_IN_PROCESS = "NOT_RIGHT_STEP_IN_PROCESS";
    public static final String SAP_ERROR = "SAP_ERROR";
    public static final String MISSING_REQUIRED_FILE_OR_INVALID_FILE_TYPE =
            "MISSING_REQUIRED_FILE_OR_INVALID_FILE_TYPE";
    public static final String FROM_DATE_AFTER_TO_DATE = "FROM_DATE_AFTER_TO_DATE";
    public static final String SELECT_PAYMENT_LOT_BEFORE_PROCESSING =
            "SELECT_PAYMENT_LOT_BEFORE_PROCESSING";
    public static final String PAYMENT_RUN_NOT_FINISHED_YET_TRY_AGAIN =
            "PAYMENT_RUN_NOT_FINISHED_YET_TRY_AGAIN";
    public static final String CREATE_PAYMENT_REQUEST_FAIL_LIST_APPROVE_NULL =
            "CREATE_PAYMENT_REQUEST_FAIL_LIST_APPROVE_NULL";
    public static final String INVALID_BASE64_STRING_FILE_DECODE =
            "INVALID_BASE64_STRING_FILE_DECODE";
    public static final String APPROVER_LIST_NO_STEP = "APPROVER_LIST_NO_STEP";
    public static final String ALL_SELECTED_ITEM_UPDATE_FAIL = "ALL_SELECTED_ITEM_UPDATE_FAIL";
    public static final String ERROR_WHILE_MAPPING_AM_INFO = "ERROR_WHILE_MAPPING_AM_INFO";
    public static final String NOT_PICK_ANY_PAYMENT_METHOD = "NOT_PICK_ANY_PAYMENT_METHOD";
    public static final String NO_APPROVED_RESULT_RETURN = "NO_APPROVED_RESULT_RETURN";
    public static final String MEDICAL_FEE_MUST_BE_LESSER_THAN_TOTAL_PAYMENT_AMOUNT =
            "MEDICAL_FEE_MUST_BE_LESSER_THAN_TOTAL_PAYMENT_AMOUNT";
    public static final String MEDICAL_FEE_CANNOT_GREATER_THAN_PREMIUM_AMOUNT =
            "MEDICAL_FEE_CANNOT_GREATER_THAN_PREMIUM_AMOUNT";
    public static final String CASH_BP_DEFAULT_NUMBER_NULL = "CASH_BP_DEFAULT_NUMBER_NULL";
    public static final String BANK_RES_FROM_SAP_NULL = "BANK_RES_FROM_SAP_NULL";
    public static final String USER_NOT_ACTIVE = "USER_NOT_ACTIVE";
    public static final String BUSINESS_PARTNER_HAS_NO_BANK = "BUSINESS_PARTNER_HAS_NO_BANK";
    public static final String UPLOAD_ATTACHMENT_TO_P2P_FAIL = "UPLOAD_ATTACHMENT_TO_P2P_FAIL";
    public static final String NO_EMPLOYEE_ACTIVE_FOUND = "NO_EMPLOYEE_ACTIVE_FOUND";
    public static final String CREATE_PAYMENT_REQUEST_FAIL_OUT_PUT_NULL =
            "CREATE_PAYMENT_REQUEST_FAIL_OUT_PUT_NULL";
    public static final String REFUSAL_FAIL = "REFUSAL_FAIL";
    public static final String NOT_FOUND_PROCESS_OPTION = "NOT_FOUND_PROCESS_OPTION";
    public static final String REFUSAL_REASON_NOT_FOUND = "REFUSAL_REASON_NOT_FOUND";
    public static final String MISSING_PROCESS_OPTION = "MISSING_PROCESS_OPTION";
    public static final String SELECT_TRANSFER_OPTION_BUT_BENEFICIAL_APP_NO_NOT_FOUND =
            "SELECT_TRANSFER_OPTION_BUT_BENEFICIAL_APP_NO_NOT_FOUND";
    public static final String MISSING_CONFIG = "MISSING_CONFIG";
    public static final String WRONG_PROCESS_OPTION = "WRONG_PROCESS_OPTION";
    public static final String APPROVE_BUT_NOT_CHOOSE_ANY_PROCESS_OPTION =
            "APPROVE_BUT_NOT_CHOOSE_ANY_PROCESS_OPTION";
    public static final String SUBMIT_APPROVED_REQ_WHILE_SALE_DECISION_PENDING =
            "SUBMIT_APPROVED_REQ_WITH_SALE_DECISION_PENDING";
    public static final String UW_PROCESSING_WHILE_NO_REFUND_IS_CHECKED =
            "UW_PROCESSING_WHILE_NO_REFUND_IS_CHECKED";
    public static final String APP_UW_SCAN_JOB_FAIL = "APP_UW_SCAN_JOB_FAIL";
    public static final String THIS_APPLICATION_NUMBER_STATUS_IS_NOT_IN_PROCESS =
            "THIS_APPLICATION_NUMBER_STATUS_IS_NOT_IN_PROCESS";
    public static final String REFUSAL_SUB_REASON_NOT_FOUND = "REFUSAL_SUB_REASON_NOT_FOUND";
    public static final String PROCESS_REQ_WHILE_SALE_DECISION_PENDING =
            "PROCESS_REQ_WITH_SALE_DECISION_PENDING";
    public static final String UW_PROCESSING_WHILE_PROCESS_OPTION_IS_NOT_REFUND =
            "UW_PROCESSING_WHILE_PROCESS_OPTION_IS_NOT_REFUND";
    public static final String ALREADY_SENT_TO_SALE_ONCE = "ALREADY_SENT_TO_SALE_ONCE";
    public static final String MODIFY_WHILE_PENDING = "MODIFY_WHILE_PENDING";
    public static final String CAN_NOT_PROCESSING_WHILE_NO_PAYMENT_IS_CHECKED =
            "CAN_NOT_PROCESSING_WHILE_NO_PAYMENT_IS_CHECKED";
    public static final String CREATE_BP_FAIL = "CREATE_BP_FAIL";
    public static final String CREATE_PAYMENT_REQUEST_FAIL_LIST_PAYMENT_DOC_NULL =
            "CREATE_PAYMENT_REQUEST_FAIL_LIST_PAYMENT_DOC_NULL";
    public static final String REVERSE_DATE_CAN_NOT_EQUAL_EFFECTIVE_DATE =
            "REVERSE_DATE_CAN_NOT_EQUAL_EFFECTIVE_DATE";
    public static final String POLICY_STATUS_INVALID_NOT_ACTIVE_NOT_REVERSE =
            "POLICY_STATUS_INVALID_NOT_ACTIVE_NOT_REVERSE";
    public static final String BUSINESS_PARTNER_DO_NOT_HAVE_THIS_IDENTIFICATION =
            "BUSINESS_PARTNER_DO_NOT_HAVE_THIS_IDENTIFICATION";
    public static final String BUSINESS_PARTNER_DO_NOT_HAVE_ANY_IDENTIFICATION =
            "BUSINESS_PARTNER_DO_NOT_HAVE_ANY_IDENTIFICATION";
    public static final String THIS_APPLICATION_NUMBER_IS_BEING_PROCESSED_BY_ANOTHER_USER_IN_SAP =
            "THIS_APPLICATION_NUMBER_IS_BEING_PROCESSED_BY_ANOTHER_USER_IN_SAP";
    public static final String CASH_ACCOUNT_QAS_NULL = "CASH_ACCOUNT_QAS_NULL";
    public static final String ALL_PAYMENT_DOC_UPDATE_FAIL = "ALL_PAYMENT_DOC_UPDATE_FAIL";
    public static final String APPLICATION_NUMBER_NOT_EXIST = "APPLICATION_NUMBER_NOT_EXIST";
    public static final String EMPTY_PROCESS_INSTANCE_ID = "EMPTY_PROCESS_INSTANCE_ID";
    public static final String DELETE_PROCESS_INSTANCE_WITHOUT_REASON =
            "DELETE_PROCESS_INSTANCE_WITHOUT_REASON";
    public static final String ADD_AUTH_BP_WHILE_PROCESS_OPTION_NOT_REFUND =
            "ADD_AUTH_BP_WHILE_PROCESS_OPTION_NOT_REFUND";
    public static final String UW_PROCESSING_WHILE_POSTING_TEXT_IS_EMPTY =
            "UW_PROCESSING_WHILE_POSTING_TEXT_IS_EMPTY";
    public static final String SUBMIT_UW_REQUEST_WITH_REFUND_OPTION_BUT_NOT_PROCESSING =
            "SUBMIT_UW_REQUEST_WITH_REFUND_OPTION_BUT_NOT_PROCESSING";
    public static final String BUSINESS_PARTNER_DO_NOT_HAVE_THIS_BANK_INFO =
            "BUSINESS_PARTNER_DO_NOT_HAVE_THIS_BANK_INFO";
    public static final String MISSING_INFO_TO_CACULATE_SLA = "MISSING_INFO_TO_CACULATE_SLA";
    public static final String GET_SLA_RULE_FAIL = "GET_SLA_RULE_FAIL";
    public static final String DECISION_HISTORY_NOT_FOUND = "DECISION_HISTORY_NOT_FOUND";
    public static final String FROM_SYSTEM_REQUEST_CODE_ALREADY_EXIST =
            "FROM_SYSTEM_REQUEST_CODE_ALREADY_EXIST";
    public static final String SIGNING_DATE_CAN_NOT_BE_EMPTY = "SIGNING_DATE_CAN_NOT_BE_EMPTY";
    public static final String THIS_POlICY_NUMBER_IS_ALREADY_REVERSED =
            "THIS_POlICY_NUMBER_IS_ALREADY_REVERSED";
    public static final String AUTO_FILL_IC_EMAIL_USING_INFO_FROM_SAP_FAIL =
            "AUTO_FILL_IC_EMAIL_USING_INFO_FROM_SAP_FAIL";
    public static final String FAIL_TO_GET_POLICY_INFO = "FAIL_TO_GET_POLICY_INFO";
    public static final String ASSIGNMENT_MODE_NOT_EXIST = "ASSIGNMENT_MODE_NOT_EXIST";
    public static final String CAN_NOT_REPAYMENT_DUE_TO_REFUSAL_FAIL =
            "CAN_NOT_REPAYMENT_DUE_TO_REFUSAL_FAIL";
    public static final String SAP_ERROR_WHILE_REFUSAL = "SAP_ERROR_WHILE_REFUSAL";
    public static final String SAP_ERROR_WHILE_REFUSAL_EMPTY_RESPONSE =
            "SAP_ERROR_WHILE_REFUSAL_EMPTY_RESPONSE";
    public static final String DESERIALIZE_ERROR_WHILE_REFUSAL = "DESERIALIZE_ERROR_WHILE_REFUSAL";
    public static final String ALREADY_CALL_REFUSAL_BUT_FAIL_TO_CHANGE_APPL_STATUS =
            "ALREADY_CALL_REFUSAL_BUT_FAIL_TO_CHANGE_APPL_STATUS";
    public static final String FAIL_TO_RETRY_PROCESS_INST_JOB = "FAIL_TO_RETRY_PROCESS_INST_JOB";
    public static final String CAN_NOT_REFUSAL_DUE_TO_CBC_APPLICATION_NUMBER =
            "CAN_NOT_REFUSAL_DUE_TO_CBC_APPLICATION_NUMBER";
    public static final String CAN_NOT_GET_BP_INFO_FROM_SAP = "CAN_NOT_GET_BP_INFO_FROM_SAP";
    public static final String SOCKET_TIME_OUT_WHILE_PUT_FILE_TO_SAP = "SOCKET_TIME_OUT_WHILE_PUT_FILE_TO_SAP";
    public static final String UNEXPECTED_ERROR_WHILE_CREATE_PAYMENT_REQUEST = "UNEXPECTED_ERROR_WHILE_CREATE_PAYMENT_REQUEST";
    public static final String UNEXPECTED_ERROR_WHILE_UPLOAD_FILE_TO_SAP = "UNEXPECTED_ERROR_WHILE_UPLOAD_FILE_TO_SAP";
    public static final String MEDICAL_FEE_CANNOT_BE_NEGATIVE = "MEDICAL_FEE_CANNOT_BE_NEGATIVE";
    public static final String REQUEST_ALREADY_SUBMIT = "REQUEST_ALREADY_SUBMIT";
    public static final String WE_DO_NOT_HAVE_HOLIDAY = "WE_DO_NOT_HAVE_HOLIDAY";
    public static final String INVALID_BASE64_STRING_FILE = "INVALID_BASE64_STRING_FILE";
    public static final String PLEASE_UPLOAD_FILE_ATTACHMENT = "PLEASE_UPLOAD_FILE_ATTACHMENT";

    static {
        messages = new HashMap<>();
        messages.put(E_INTERNAL_SERVER, "Server không phản hồi");
        messages.put(COMMON_NULL_STRING, "Yêu cầu không được để trống");
        messages.put(COMMON_DUPLICATE_CODE, "Mã danh mục con là unique");
        messages.put(COMMON_CATEGORY_DUPLICATE_CODE, "Mã danh mục cha là unique");
        messages.put(COMMON_EXIST, "Danh mục không tồn tại");
        messages.put(E_EXPORT_001, "Lỗi xuất excel");
        messages.put(COMMON_LESS_THAN_0, "Số thứ tự không được nhỏ hơn 0");
        messages.put(COMMON_POSITION, "Vị trí phải khác nhau");
        messages.put(COMMON_DELETED, "Bản ghi đã xóa");
        messages.put(COMMON_ONLY_ONE_DEFAULT, "Chỉ được 1 giá trị default");
        messages.put(
                ABILITY_NULL_STRING,
                "Không chấp nhận giá trị null code,name,capacity group code,classify code,max level");
        messages.put(ABILITY_EXIST, "Năng lực đã tồn tại");
        messages.put(ABILITY_NOT_EXIST, "Năng lực không tồn tại");
        messages.put(ABILITY_LEVEL, "3=<LEVEL<=5");
        messages.put(ABILITY_EXIST_GROUP, "Nhóm năng lực không tồn tại");
        messages.put(ABILITY_EXIST_CLASSIFY, "Loại năng lực không tồn tại");
        messages.put(ABILITY_INVALID_SIZE, "Danh sách cấp bậc không hợp lệ");
        messages.put(ABILITY_NULL_LEVEL_REQUIRED, "Code and name required not null");
        messages.put(LEVEL_UNIQUE_CODE, "Level code required unique");
        messages.put(ExceptionUtils.E_BODY_INFO_MISSING, "Không nhận được RequestBody");
        messages.put(ExceptionUtils.E_COMMON_DUPLICATE_CODE, "Code %s đã tồn tại");
        messages.put(ExceptionUtils.E_COMMON_NOT_EXISTS_CODE, "Code %s không tồn tại");
        messages.put(ExceptionUtils.FORBIDDEN, "Không có quyền truy cập");
        messages.put(
                ExceptionUtils.INVALID_POLICY_NUMBER, "Số hợp đồng ko tồn tại hoặc thiếu thông tin.");
        messages.put(
                ExceptionUtils.SIGNING_DATE_CAN_NOT_OVER_21_DAY_FROM_ACK_DATE,
                "Hợp đồng đã quá 21 ngày đê hủy.");
        messages.put(ExceptionUtils.SAP_NOT_RESPONSE, "SAP không phảin hồi");
        messages.put(ExceptionUtils.REQUEST_TYPE_NOT_EXIST, "Loại yêu cầu không tồn tại");
        messages.put(
                ExceptionUtils.NUM_OF_FILES_AND_TYPES_NOT_EQUAL,
                "Số lượng file và loại file đẩy lên phải bằng nhau !  ");
        messages.put(
                ExceptionUtils.INVALID_FILE_TYPES_STRING_REQUEST,
                "Chuỗi loại file không hợp lệ ! types : invalid  ");
        messages.put(ExceptionUtils.REQUEST_NOT_EXIST, "Yêu cầu không tồn tại.  ");
        messages.put(
                ExceptionUtils.INVALID_FILE_TYPE_UPLOAD,
                "Chỉ chấp nhận file pdf và image (ngoại trừ image/webp)");
        messages.put(ExceptionUtils.MISSING_REQUIRED_FILE, "Upload file ZSAP_CA bắt buộc ");
        messages.put(ExceptionUtils.MAX_FILE_SIZE_UPLOAD, "Chỉ được upload file nhỏ hơn <=10 MB  ");
        messages.put(
                ExceptionUtils.REVERSAL_FAIL,
                "Reversal thất bại . Số hợp dồng không tồn tại hoặc đã bị reversal . %s ");
        messages.put(
                ExceptionUtils.SEARCH_PARAM_VALUE_NOT_MATCH_ANY_ENUM_VALUE,
                " Gía trị tìm kiếm  %s  không tồn tại trong hệ thống !  ");
        messages.put(ExceptionUtils.USER_NOT_EXIST, " User name  %s  không tồn tại trong hệ thống !  ");
        messages.put(
                ExceptionUtils.INVALID_POLICY_NUMBER_NO_FLP_DATE,
                " Số hợp đồng không hơp lệ : ko có FLP_DT !  ");
        messages.put(
                ExceptionUtils.INVALID_POLICY_NUMBER_NO_POLICY_NUMBER_RETURN,
                " Số hợp đồng không hơp lệ : ko có policy number trả về hoặc hợp đồng đã revered !  ");
        messages.put(
                ExceptionUtils.POLICY_NUMBER_REQUEST_EXIST,
                "Yêu cầu hủy của số hợp đồng %s đã tồn tại .  ");
        messages.put(ExceptionUtils.TOKEN_EXPIRES, "MB bank token đã hết hạn ! ");
        messages.put(ExceptionUtils.CAN_NOT_FORWARD_TO_YOURSELF, "Tự forward cho chính mình  ???! ");
        messages.put(ExceptionUtils.CAN_NOT_FORWARD, "Yêu cầu không phải của bạn không thể forwwad . ");
        messages.put(ExceptionUtils.ATTACHMENT_NOT_EXIST, "Attachment ko tồn tại. ");
        messages.put(ExceptionUtils.EXPORT_REPORT_FAIL, "EXPORT_REPORT_FAIL ");
        messages.put(ExceptionUtils.CREATE_PAYMENT_REQUEST_FAIL, "CREATE_PAYMENT_REQUEST_FAIL ");
        messages.put(
                ExceptionUtils.NOT_CHOSEN_OR_ADD_NEW_BANK_ACCOUNT_YET,
                "CHưa chọn tài khoản ngân hàng hoặc thêm thất bại");
        messages.put(ExceptionUtils.CREATE_PAYMENT_RUN_FAIL, "tạo payment run thất bại");
        messages.put(ExceptionUtils.UPLOAD_ATTACHMENT_TO_SAP_FAIL, "Upload file lên sap thất bại");
        messages.put(ExceptionUtils.NO_SEARCH_RESULT_FOUND, "Không tìm thấy kết quả");
        messages.put(ExceptionUtils.BANK_ACCOUNT_HAD_EXIST, "Tài khoản ngân hàng đã tồn tại");
        messages.put(
                ExceptionUtils.MEDICAL_FEE_CANNOT_GREATER_THAN_POA_AND_PREMIUM_AMOUNT,
                "Phí khám lớn hơn POA và premium account");
        messages.put(ExceptionUtils.FILTER_CAN_NOT_NULL, "Vui lòng chọn yêu cầu tìm kiếm");
        messages.put(ExceptionUtils.USED_TO_PAYMENT_RUN, "Đã từng payment run, không thế chạy lại");
        messages.put(
                ExceptionUtils.CREATE_PAYMENT_RUN_FAIL_RESPONSE_LIST_DOC_NULL,
                "tạo payment run thất bại danh sách payment doc trả về rỗng");
        messages.put(
                ExceptionUtils.PAYMENT_RUN_RESPONSE_DO_NOT_CONTAIN_DOC_11,
                "PAYMENT RUN RESPONSE DO NOT CONTAIN DOC 11");
        messages.put(ExceptionUtils.INVALID_INPUT_BANK_ACCOUNT_NUMBER, "Account doesn't exist");
        messages.put(
                ExceptionUtils.BANK_ACCOUNT_DOES_NOT_EXIST_OP_SAP, "Tài khoản không tồn tại trên sap");
        messages.put(ExceptionUtils.APPLY_NEW_RULE_FAIL, "APPLY NEW RULE FAIL");
        messages.put(
                ExceptionUtils.BPM_COMPLETE_TASK_WITH_NO_VARIABLES,
                "Chưa đẩy biến vào process khi complete userTask");
        messages.put(ExceptionUtils.NOT_PDF, "Tệp gửi lên không phải PDF");
        messages.put(ExceptionUtils.FILE_IS_EMPTY, "Tệp gửi lên không có dữ liệu hoặc không tồn tại!");
        messages.put(ExceptionUtils.APPLICATION_NUMBER_NOT_EXIST, "Số hồ sơ không tồn tại!");
        messages.put(
                ExceptionUtils.SAP_ERROR_WHILE_REFUSAL_EMPTY_RESPONSE,
                "RESPONSE rỗng khi gọi Refusal hồ sơ");
        messages.put(
                ExceptionUtils.CAN_NOT_REPAYMENT_DUE_TO_REFUSAL_FAIL,
                "Không thể Repayment (Payment run) do huỷ hồ sơ (Refusal) thất bại");
        messages.put(ExceptionUtils.SAP_ERROR_WHILE_REFUSAL, "Lỗi SAP khi huỷ hồ sơ (Refusal)");
        messages.put(ExceptionUtils.EMPTY_PAYMENT_NUMBER, "Chưa ghi nhận được số Payment number");
        messages.put(ExceptionUtils.PAYMENT_METHOD_NOT_VALID, "Phương thức thanh toán không hợp lệ");
        messages.put(
                ExceptionUtils.DESERIALIZE_ERROR_WHILE_REFUSAL,
                "Không thể mapping dữ liệu từ SAP trả về khi gọi API huỷ hồ sơ");
        messages.put(
                ExceptionUtils.ASSIGNMENT_MODE_NOT_EXIST,
                "Cách chia yêu cầu UW tạo từ job tự động này chưa được định nghĩa");
        messages.put(
                ExceptionUtils.ALREADY_CALL_REFUSAL_BUT_FAIL_TO_CHANGE_APPL_STATUS,
                "Đã gọi Refusal hồ sơ nhưng trạng thái không thay đổi");
        messages.put(
                ExceptionUtils.CAN_NOT_REFUSAL_DUE_TO_CBC_APPLICATION_NUMBER,
                "Không thể huỷ hồ sơ này do số hồ sơ đã CBC, vui lòng tiến hành huỷ hồ sơ trên SAP sau đó tiếp tục thao tác trên BPM");
        messages.put(
                ExceptionUtils.REQUEST_ALREADY_SUBMIT, "Yêu cầu đã được nộp và trong quá trình xử lý");
    }

    private ExceptionUtils() {
    }
}
