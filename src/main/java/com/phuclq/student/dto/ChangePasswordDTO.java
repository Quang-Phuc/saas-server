package com.phuclq.student.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String password;
    private String passwordNew;
    private String passwordConfirm;

}
