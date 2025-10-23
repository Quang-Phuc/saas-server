package com.phuclq.student.dto.content;


import java.time.LocalDateTime;

public interface ContentResult {

    Long getId();

    String getTitle();

    String getCreatedBy();

    LocalDateTime getCreatedDate();

}
