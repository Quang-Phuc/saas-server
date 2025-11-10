package com.phuclq.student.dto;


import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO đại diện cho từng kỳ thanh toán trong hợp đồng cầm cố.
 * Dữ liệu này sẽ được trả về trong API /api/pledge-contract/{id}.
 */
@Data
public class PaymentScheduleDto {

    /** Kỳ thứ mấy (ví dụ: 1, 2, 3...) */
    private Integer periodNumber;

    /** Ngày đến hạn thanh toán (định dạng yyyy-MM-dd) */
    private String dueDate;

    /** Số tiền lãi phải trả kỳ này */
    private BigDecimal interestAmount;

    /** Số tiền gốc phải trả kỳ này */
    private BigDecimal principalAmount;

    /** Tổng tiền phải trả kỳ này (gốc + lãi) */
    private BigDecimal totalAmount;

    /** Trạng thái kỳ thanh toán: PENDING, PAID, OVERDUE */
    private String status;

    /** Ngày thực tế khách thanh toán (nếu có) */
    private String paidDate;
}
