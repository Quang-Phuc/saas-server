package com.phuclq.student.dto;

import com.phuclq.student.dto.PledgeContractListResponse;
import com.phuclq.student.types.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PledgeContractListResponseImpl implements PledgeContractListResponse {

    private final Long id;
    private final String contractCode;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private final String customerName;
    private final String phoneNumber;
    private final String assetName;
    private final BigDecimal loanAmount;
    private final BigDecimal totalPaid;
    private final BigDecimal remainingPrincipal;
    private final LoanStatus status;
    private final String follower;
    private final String pledgeStatus;

    public PledgeContractListResponseImpl(
            Long id,
            String contractCode,
            LocalDate loanDate,
            LocalDate dueDate,
            String customerName,
            String phoneNumber,
            String assetName,
            BigDecimal loanAmount,
            BigDecimal totalPaid,
            BigDecimal remainingPrincipal,
            LoanStatus status,
            String follower,
            String pledgeStatus
    ) {
        this.id = id;
        this.contractCode = contractCode;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.assetName = assetName;
        this.loanAmount = loanAmount;
        this.totalPaid = totalPaid;
        this.remainingPrincipal = remainingPrincipal;
        this.status = status;
        this.follower = follower;
        this.pledgeStatus = pledgeStatus;
    }

    @Override public Long getId() { return id; }
    @Override public String getContractCode() { return contractCode; }
    @Override public LocalDate getLoanDate() { return loanDate; }
    @Override public LocalDate getDueDate() { return dueDate; }
    @Override public String getCustomerName() { return customerName; }
    @Override public String getPhoneNumber() { return phoneNumber; }
    @Override public String getAssetName() { return assetName; }
    @Override public BigDecimal getLoanAmount() { return loanAmount; }
    @Override public BigDecimal getTotalPaid() { return totalPaid; }
    @Override public BigDecimal getRemainingPrincipal() { return remainingPrincipal; }
    @Override public LoanStatus getStatus() { return status; }
    @Override public String getFollower() { return follower; }
    @Override public String getPledgeStatus() { return pledgeStatus; }
}
