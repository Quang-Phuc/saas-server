package com.phuclq.student.dto;

import com.phuclq.student.types.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PledgeContractResponse {
    private Long contractId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private String customerName;
    private String customerPhone;
    private String assetName;
    private String assetType;
    private BigDecimal loanAmount;
    private String interestRate;
    private BigDecimal remainingPrincipal;
    private LoanStatus status;
    private String storeId;
}
