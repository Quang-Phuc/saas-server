package com.phuclq.student.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanDto {
    private String loanDate; // dạng "yyyy-MM-dd"
    private String contractCode;
    private BigDecimal loanAmount;
    private Integer interestTermValue;
    private String interestTermUnit;
    private BigDecimal interestRateValue;
    private String interestRateUnit;
    private Integer paymentCount;
    private String interestPaymentType;
    private String note;
    private String loanStatus;
    private String partnerType;
    private String follower;
    private String customerSource;
}
