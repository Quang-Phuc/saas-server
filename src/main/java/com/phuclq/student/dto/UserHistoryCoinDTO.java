package com.phuclq.student.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserHistoryCoinDTO {
    private Integer id;
    private Integer userId;
    private Integer coin;
    private Timestamp activityDate;
    private Integer transaction;
    private String description;
    private String email;
    private String userName;
}
