package com.phuclq.student.dto;

import lombok.Data;

@Data
public class HistoryFileResultDto {

    private int id;
    private String title;
    private String fileAvatar;
    private String fileDemo;
    private String fileUpload;
    private String createDate;
    private Integer view;
    private Integer downloading;
    private Double money;
}
