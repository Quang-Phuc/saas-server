package com.phuclq.student.dto;

import lombok.Data;

@Data
public class FeesDto {
    private FeeItemDto warehouseFee;
    private FeeItemDto storageFee;
    private FeeItemDto riskFee;
    private FeeItemDto managementFee;
}