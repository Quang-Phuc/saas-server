package com.phuclq.student.dto;


import com.phuclq.student.types.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PledgeSearchRequest {
    private Long id;
    private String contractCode;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private String fullName;
    private String phoneNumber;
    private String assetNames;
    private BigDecimal loanAmount;
    private BigDecimal totalPaid;
    private BigDecimal remainingPrincipal;
    private String loanStatus;
    private String storeId;
    private int page = 0; // Trang hiện tại
    private int size = 10; // Số record/trang
}
