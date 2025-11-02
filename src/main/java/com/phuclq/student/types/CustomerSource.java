package com.phuclq.student.types;


/**
 * Định nghĩa nguồn khách hàng.
 * Tương ứng với trường 'customerSource' trong bảng 'loans'.
 * Giá trị DB: COLLABORATOR, ALL.
 */
public enum CustomerSource {

    COLLABORATOR("ctv", "Giới thiệu bạn bè"),
    ALL("all", "Tất cả");

    private final String dbValue;
    private final String description;

    CustomerSource(String dbValue, String description) {
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