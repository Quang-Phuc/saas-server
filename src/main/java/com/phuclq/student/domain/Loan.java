package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
public class Loan extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // <-- ID kiểu Long

    // (Tất cả các trường từ LoanDto)
    private String assetName;
    private String assetType;
    private LocalDate loanDate;
    private String contractCode;
    private BigDecimal loanAmount;
    private Integer interestTermValue;
    private String interestTermUnit;
    private BigDecimal interestRateValue;
    private String interestRateUnit;
    private Integer paymentCount;
    private String interestPaymentType;
    @Column(columnDefinition = "TEXT")
    private String note;
    private String loanStatus;
    private String partnerType;
    private String follower;
    private String customerSource;
}