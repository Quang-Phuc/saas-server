package com.phuclq.student.types;


/**
 * Định nghĩa đơn vị tính cho kỳ hạn tính lãi (interestTermValue).
 * Tương ứng với trường 'interestTermUnit' trong bảng 'loans'.
 */
public enum InterestTermUnit {

    DAY("Ngày"),
    WEEK("Tuần"),

    /**
     * Tháng định kỳ, thường dùng cho các hợp đồng cố định ngày trả (ví dụ: ngày 5 hàng tháng).
     */
    PERIODIC_MONTH("Tháng định kỳ"),

    MONTH("Tháng");

    private final String description;

    InterestTermUnit(String description) {
        this.description = description;
    }

    /**
     * Lấy mô tả thân thiện của đơn vị.
     */
    public String getDescription() {
        return description;
    }
}
