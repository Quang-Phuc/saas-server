package com.phuclq.student.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class PledgeSearchRequest {

    String keyword;
    String follower;
    LocalDate toDate;
    private String loanStatus;
    private String pledgeStatus;
    private String storeId;
}
