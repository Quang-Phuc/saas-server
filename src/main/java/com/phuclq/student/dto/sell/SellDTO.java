package com.phuclq.student.dto.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellDTO {
    // Fields from Sell
    private Long id;
    private Long sellCategoryId;
    private Integer wardId;
    private Integer districtId;
    private Integer provinceId;
    private String content;
    private String title;
    private Integer approverId;
    private Timestamp approvedDate;
    private Boolean isDeleted;
    private Double moneyTop;
    private LocalDateTime startMoneyTop;
    private LocalDateTime endMoneyTop;
    private Integer totalCard;
    private Integer deleteId;
    private Timestamp deleteDate;

    private Long categoryId;
    private String categoryName;

    private String districtName;
    private String districtPrefix;
    private Integer districtProvinceId;

    private String wardName;
    private String wardPrefix;
    private Integer wardProvinceId;
    private Integer wardDistrictId;

    private String provinceName;
    private String provinceCode;

    private Double price;

    private Double quantity;

    private String createdDate;

    private String userName;

    private String phone;

}
