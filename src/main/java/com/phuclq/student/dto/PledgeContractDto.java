package com.phuclq.student.dto;

import lombok.Data;

@Data
public class PledgeContractDto {
    private String storeId;
    private CustomerDto customer;
    private LoanDto loan;
    private CollateralDto collateral;
    private FeesDto fees;
}