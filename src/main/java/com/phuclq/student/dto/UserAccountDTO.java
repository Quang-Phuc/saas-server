package com.phuclq.student.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserAccountDTO {
    private String userName;
    @NotBlank
    private String password;
    private String email;
    private String phone;
    private String fullName;
    @NotBlank
    private String rePassword;

    private String userID;

    private Long schoolId;

    private String referredBy;

}
