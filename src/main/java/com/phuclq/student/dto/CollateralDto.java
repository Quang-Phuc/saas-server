package com.phuclq.student.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CollateralDto {
    private String assetName;
    private Long assetType;
    private String assetCode;
    private BigDecimal valuation;
    private Long warehouseId;
    private String assetNote;

    private List<AssetTypeResponse.AttributeDto> attributes;
}
