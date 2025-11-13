package com.phuclq.student.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PayInterestRequest {
    private Integer periodNumber;     // FE gửi kỳ hiện tại (điểm bắt đầu)
    private LocalDate payDate;
    private BigDecimal amount;
    private String paymentMethod;
    private Long id;                  // paymentScheduleId (không dùng trực tiếp)
    private String note;
}
