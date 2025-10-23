package com.phuclq.student.dto;

import java.sql.Timestamp;

public interface UserHistoryCoinResult {
    Integer getId();

    Integer getUser_id();

    Integer getCoin();

    Timestamp getActivity_date();

    Integer getTransaction();

    String getDescription();

    String getEmail();

    String getUser_name();
}
