package com.phuclq.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithStoreDTO {
    private Long userId;
    private String fullName;
    private String phone;
    private String email;
    private Integer roleId;

    private Long storeId;
    private String storeName;
    private String storeAddress;

    private String createdDate;
}
