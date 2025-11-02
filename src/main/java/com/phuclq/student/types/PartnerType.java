package com.phuclq.student.types;


/**
 * Định nghĩa loại đối tác liên quan đến hợp đồng.
 * Tương ứng với trường 'partnerType' trong bảng 'loans'.
 * Giá trị DB: CUSTOMER, CREDITOR, v.v.
 */
public enum PartnerType {

    CUSTOMER("khach_hang", "Cá nhân"),
    CREDITOR("chu_no", "Chủ nợ"),
    FOLLOWER("nguoi_theo_doi", "Người theo dõi"),
    ALL("all", "Tất cả");

    private final String dbValue;
    private final String description;

    PartnerType(String dbValue, String description) {
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