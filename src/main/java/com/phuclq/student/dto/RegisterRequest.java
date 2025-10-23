package com.phuclq.student.dto;


import lombok.Data;

@Data
public class RegisterRequest {
    private String storeName;
    private String phone;
    private String password;
}

