package com.phuclq.student.dto.blog;

import com.phuclq.student.dto.RequestFileDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogRequest {

    List<RequestFileDTO> files;

    private Long id;

    private Long categoryBlogId;

    private String title;

    private String search;

    private String content;

    private Boolean isDeleted;

    private Boolean status;

    private Integer approve;

    private String type;

    private String description;

    private String alt;

    private String url;

    private Integer activityId;

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private List<Long> ids;
    private String idUrl;
    private int page;
    private int size;


}
