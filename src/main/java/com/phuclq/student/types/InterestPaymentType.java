package com.phuclq.student.types;


/**
 * Định nghĩa các hình thức thanh toán lãi/gốc cho hợp đồng cầm cố (Loan).
 * Giá trị này tương ứng với trường 'interestPaymentType' trong bảng 'loans'.
 */
public enum InterestPaymentType {

    /**
     * Trả lãi định kỳ, gốc được chuộc (thanh toán) vào cuối kỳ.
     * Tương ứng với value "Truoc" trong logic cũ.
     */
    PERIODIC_INTEREST("Trả lãi định kỳ – Chuộc gốc cuối kỳ"),

    /**
     * Trả góp bao gồm cả gốc và lãi, thanh toán hằng kỳ.
     */
    INSTALLMENT("Trả góp gốc + lãi hằng kỳ"),

    /**
     * Thanh toán toàn bộ tiền gốc và lãi một lần duy nhất vào cuối kỳ.
     */
    LUMP_SUM_END("Trả cuối kỳ (cả gốc + lãi 1 lần)"),

    /**
     * Hình thức gia hạn hợp đồng (đáo hạn).
     */
    RENEWAL("Gia hạn hợp đồng (đáo hạn)");

    private final String description;

    InterestPaymentType(String description) {
        this.description = description;
    }

    /**
     * Hàm tiện ích để kiểm tra tính hợp lệ của giá trị String nhận từ Frontend.
     */
    public static boolean isValid(String value) {
        if (value == null) return false;
        try {
            // Chuyển String thành Enum để kiểm tra
            InterestPaymentType.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Lấy mô tả thân thiện của hình thức thanh toán.
     */
    public String getDescription() {
        return description;
    }
}