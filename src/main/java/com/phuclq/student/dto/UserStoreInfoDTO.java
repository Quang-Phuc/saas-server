package com.phuclq.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStoreInfoDTO {
    private Integer id;
    private String fullName;
    private String phone;
}
