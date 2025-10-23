package com.phuclq.student.dto;

import lombok.Data;

@Data
public class FileSearchRequest {
    private Integer category;
    private Integer specialization;
    private Integer school;
    private String title;
    private Boolean isVip;
    private Float price;
}
