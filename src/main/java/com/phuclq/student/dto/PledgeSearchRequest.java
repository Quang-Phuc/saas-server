package com.phuclq.student.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PledgeSearchRequest {

    private String keyword;
    private String follower;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String loanStatus;
    private String pledgeStatus;
    private Long storeId;
    private int page;
    private int size;
}
