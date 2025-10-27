package com.phuclq.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Integer id;
    private String name; // Using fullName from User entity
    private Long storeId; // Include storeId if needed for selection dialog
}