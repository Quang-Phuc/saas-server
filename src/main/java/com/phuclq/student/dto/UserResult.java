package com.phuclq.student.dto;

import java.sql.Date;
import java.sql.Timestamp;

public interface UserResult {

    Integer getId();

    String getUser_name();

    String getPassword();

    String getEmail();

    String getPhone();

    Boolean getIs_deleted();

    Integer getRole_id();

    Boolean getIs_enable();

    Timestamp getCreated_date();

    Double getTotal_coin();

    Date getBirthDay();

    String getFullName();

    String getGender();

    String getAddress();

    String getIntroduction();

    String getImage();

    Integer getIndustryId();

    String getReferralCode();
}
