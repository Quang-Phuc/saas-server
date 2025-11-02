package com.phuclq.student.dto;

import lombok.Data;

@Data
public class CollateralDto {
    // (Các trường khớp với ...collateralInfo)
    private Long valuation;
    private String licensePlate;
    private String chassisNumber;
    private String engineNumber;
    private Long warehouseId; // <-- Kiểu Long
    private String assetCode;
    private String assetNote;
}