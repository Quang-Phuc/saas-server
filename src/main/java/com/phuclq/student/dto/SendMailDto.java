package com.phuclq.student.dto;

import lombok.Data;

@Data
public class SendMailDto {
    private String name ;
    private String email;
    private  Double  money;
    private  String  sub;
    private  String  password;
}
