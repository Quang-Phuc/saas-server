package com.phuclq.student.dto.sell;

import java.security.Timestamp;
import java.time.LocalDateTime;

public interface SellDTOIn {
    // Fields from Sell
    Long getId();

    Long getSellCategoryId();

    Integer getWardId();

    Integer getDistrictId();

    Integer getProvinceId();

    String getContent();
    
    String getPhone();

    String getTitle();

    Integer getApproverId();

    Timestamp getApprovedDate();

    Boolean getIsDeleted();

    Double getMoneyTop();

    LocalDateTime getStartMoneyTop();

    LocalDateTime getEndMoneyTop();

    Integer getTotalCard();

    Integer getDeleteId();

    Timestamp getDeleteDate();

    // Additional fields
    Long getCategoryId();

    String getCategoryName();

    String getDistrictName();

    String getDistrictPrefix();

    Integer getDistrictProvinceId();

    String getWardName();

    String getWardPrefix();

    Integer getWardProvinceId();

    Integer getWardDistrictId();

    String getProvinceName();

    String getProvinceCode();

    public Double getPrice() ;

    public Double getQuantity() ;

    public String getUserName();

    public LocalDateTime getCreatedDate() ;
}
