package com.phuclq.student.dto;

import lombok.Data;

@Data
public class UsersSearchRequest {
    private String startDate;
    private String endDate;
    private String userName;
    private String email;
    private String phone;

}
