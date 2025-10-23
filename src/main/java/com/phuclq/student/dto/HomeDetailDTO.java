package com.phuclq.student.dto;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface HomeDetailDTO {


    Double getSalaryEnd();

    Double getSalaryStart();

    BigInteger getId();

    String getCreateBy();

    String getFullName();

    String getWardId();

    Integer getDistrictId();

    Integer getProvinceId();

    String getGender();

    Double getPrice();

    Integer getLevel();

    String getType();

    String getContent();

    String getAddress();

    Boolean getDeleted();

    String getPhone();

    String getEmail();

    String getAcreage();

    Integer getJobType();

    String getJobName();

    String getCompanyName();

    String getCountNumberJob();

    Timestamp getDeadline();

    String getSearch();

    String getTitle();

    String getProvinceName();

    String getDistrictName();

    String getWardName();

    String getNameUser();

    Boolean getLike();

    Boolean getClosed();

    Boolean getShared();

    Boolean getArCondition();

    Boolean getFridge();

    Boolean getWashingMachine();

    String getName();

    LocalDateTime getCreatedDate();
}
