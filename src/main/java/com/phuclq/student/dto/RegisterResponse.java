package com.phuclq.student.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Long storeId;
    private String storeName;
    private String ownerPhone;
    private String plan;
    private String expiredAt;
}
