package com.phuclq.student.dto.sale;


import java.time.LocalDateTime;

public interface ContentResult {

    Long getId();

    String getTitle();

    String getCreatedBy();

    LocalDateTime getCreatedDate();

}
