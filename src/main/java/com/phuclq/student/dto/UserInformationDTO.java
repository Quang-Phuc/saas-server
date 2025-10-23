package com.phuclq.student.dto;

import lombok.Data;

@Data
public class UserInformationDTO {


    String userName;
    Integer specialize;
    String yourself;
    private String birthDate;//yyyy-MM-dd
    private Short gender;
    private Integer schoolId;
    private Integer address;
    private String phone;
}

