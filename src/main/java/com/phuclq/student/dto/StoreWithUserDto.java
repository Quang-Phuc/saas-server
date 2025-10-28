package com.phuclq.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreWithUserDto {
    private Long storeId;
    private String storeName;
    private String notes;
    private String storeAddress;
    private Integer userId;
    private String userFullName;
    private String userPhone;
    private String userEmail;
    private Integer userRoleId;
    private LocalDateTime createdDate;
}
