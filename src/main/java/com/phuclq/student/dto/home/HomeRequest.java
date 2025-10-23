package com.phuclq.student.dto.home;

import com.phuclq.student.dto.RequestFileDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HomeRequest {

    List<RequestFileDTO> files;
    private Long id;
    private String type;
    private String title;
    private Integer wardId;
    private Integer districtId;
    private Integer provinceId;
    private Double price;
    private String address;
    private String acreage;
    private Boolean closed;
    private Boolean shared;
    private String content;
    private String phone;
    private String nameUser;
    private String email;
    private Boolean isDeleted;
    private String search;
    private Boolean airCondition;
    private Boolean fridge;
    private Boolean washingMachine;
    private String name;
    private Integer activityId;
    private Integer approve;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private List<Long> ids;


}
