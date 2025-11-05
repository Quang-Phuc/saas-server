package com.phuclq.student.dto;


import com.phuclq.student.domain.AssetTypeAttribute;
import lombok.Data;

import java.util.List;

@Data
public class AssetTypeDTO {

    private Integer id;

    private String typeCode;

    private String typeName;

    private String status;

    private Long storeId;

    private List<AssetTypeAttribute> attributes; // chỉ dùng để nhận JSON


}
