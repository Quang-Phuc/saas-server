package com.phuclq.student.dto.banner;


import java.time.LocalDateTime;

public interface BannerResult {

    Long getId();

    String getTitle();

    String getCreatedBy();

    LocalDateTime getCreatedDate();

}
