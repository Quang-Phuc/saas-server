package com.phuclq.student.types;

/**
 * Định nghĩa tình trạng khoản vay (Loan Status).
 * Tương ứng với trường 'loanStatus' trong bảng 'loans'.
 * Giá trị DB: NORMAL, NORMAL_2, BAD_DEBT, v.v.
 */
public enum LoanStatus {

    NORMAL("NORMAL", "Chưa vay"),
    NORMAL_2("NORMAL_2", "Đang vay"),
    RISKY("RISKY", "Nợ rủi ro"),
    BAD_DEBT_R2("BAD_DEBT_R2", "Nợ R2"),
    BAD_DEBT_R3("BAD_DEBT_R3", "Nợ R3"),
    BAD_DEBT("BAD_DEBT", "Nợ xấu");

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
