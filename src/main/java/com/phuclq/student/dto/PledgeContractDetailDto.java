package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class PledgeContractDetailDto {
    private Long storeId;
    private CustomerDto customer;
    private LoanDto loan;
    private FeesDto fees;

    // JSON trả về "collateral": [ {...}, {...} ]
    private List<CollateralDto> collateral;
}
