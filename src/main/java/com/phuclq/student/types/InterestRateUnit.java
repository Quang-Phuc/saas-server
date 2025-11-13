package com.phuclq.student.types;


/**
 * Định nghĩa các đơn vị tính lãi suất (Interest Rate Unit) được sử dụng trong hợp đồng cầm đồ/vay vốn.
 * Giá trị này tương ứng với các value được gửi từ form Angular Material (mat-select).
 */
public enum InterestRateUnit {

    /**
     * Lãi suất tính theo số tiền lãi trên mỗi triệu VNĐ, mỗi ngày.
     * (Frontend value: INTEREST_PER_MILLION_PER_DAY)
     */
    INTEREST_PER_MILLION_PER_DAY("Lãi/Triệu/Ngày"),

    /**
     * Lãi suất tính theo phần trăm mỗi tháng.
     * (Frontend value: INTEREST_PERCENT_PER_MONTH)
     */
    INTEREST_PERCENT_PER_MONTH("Lãi%/Tháng"),

    /**
     * Lãi suất tính theo số tiền lãi mỗi ngày.
     * (Frontend value: INTEREST_PER_DAY)
     */
    INTEREST_PER_DAY("Lãi%/Ngày");

    private final String description;

    InterestRateUnit(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Tùy chọn: Thêm phương thức tìm kiếm theo description nếu cần
    public static InterestRateUnit fromDescription(String description) {
        for (InterestRateUnit unit : InterestRateUnit.values()) {
            if (unit.description.equalsIgnoreCase(description)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }
}
