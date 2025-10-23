package com.phuclq.student.dto.sell;

import com.phuclq.student.dto.RequestFileDTO;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SellRequest {

    List<RequestFileDTO> files;
    private Long id;
    private String idUrl;
    private String title;
    private Integer wardId;
    private Integer districtId;
    private Integer provinceId;
    private String name;
    private String search;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean status;
    private Integer approve;
    private Long sellCategoryId;
    private String content;
    private String phone;
    private Integer approverId;
    private Timestamp approvedDate;
    private Integer activityId;
    private List<Long> ids;


}
