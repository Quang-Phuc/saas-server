package com.phuclq.student.dto;


import lombok.Data;
import java.util.List;

@Data
public class AssetTypeResponse {
    private Integer id;
    private String name;
    private String description;
    private Long storeId;
    private List<AttributeDto> attributes; // danh sách label động

    @Data
    public static class AttributeDto {
        private Integer id;
        private String label;
        private String value;
        private Long assetTypeId;
    }
}
