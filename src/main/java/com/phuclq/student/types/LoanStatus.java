package com.phuclq.student.types;


/**
 * Định nghĩa tình trạng khoản vay (Loan Status).
 * Tương ứng với trường 'loanStatus' trong bảng 'loans'.
 * Giá trị DB: BINH_THUONG, NO_XAU, v.v.
 */
public enum LoanStatus {

    BINH_THUONG("Bình thường", "Chưa vay"),
    BINH_THUONG_2("Bình thường 2", "Đang vay"),
    NO_XAU("Nợ xấu", "Nợ xấu"),
    NO_RUI_RO("Nợ rủi ro", "Nợ rủi ro"),
    NO_R2("Nợ R2", "Nợ R2"),
    NO_R3("Nợ R3", "Nợ R3");

    private final String dbValue;
    private final String description;

    LoanStatus(String dbValue, String description) {
        this.dbValue = dbValue;
        this.description = description;
    }

    /** Lấy giá trị chuỗi (DB value) để lưu vào cơ sở dữ liệu. */
    public String getDbValue() {
        return dbValue;
    }

    /** Lấy mô tả thân thiện (hiển thị trên UI/Angular). */
    public String getDescription() {
        return description;
    }
}