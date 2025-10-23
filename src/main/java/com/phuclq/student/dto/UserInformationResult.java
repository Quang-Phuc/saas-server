package com.phuclq.student.dto;

import java.sql.Timestamp;

public interface UserInformationResult {

    String getUserName();

    Timestamp getBirthDate();

    Short getGender();

    Integer getAddress();

    String getPhone();

    Integer getSpecialized();

    String getYourself();

    Integer getSchoolId();
}

