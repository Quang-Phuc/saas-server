package com.phuclq.student.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FileHomePageRequest {
    private String title;
    private Boolean isVip;
    private Integer priceStart;
    private Integer priceEnd;
    private Integer priceTo;
    private Integer school;
    private Integer industry;
    private Integer page;
    private Integer size;
    private Integer sizeFile;
    private String order;
    private Integer orderType;
    private String type;
    private Integer orderBy;
    private Integer categoryId;
    private String search;
    private List<Long> categoryIds;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Integer approve;
    private Boolean isLike;
    private Boolean isCard;
    private Boolean isDownload;
    private Boolean isUser;
    private List<Integer> fileIds;
    private Integer activityId;
    private Integer fileId;
    private Boolean isBase64;
    private Boolean isApprove;
    private Integer loginId;
    private Integer transaction;
    private Integer status;
    private String idUrl;
    private String idUrlCategory;
    private Boolean home;
    private Boolean isDuplicate;

}
