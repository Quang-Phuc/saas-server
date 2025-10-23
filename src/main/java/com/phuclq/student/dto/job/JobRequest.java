package com.phuclq.student.dto.job;

import com.phuclq.student.dto.RequestFileDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class JobRequest {

    List<RequestFileDTO> files;
    private Long id;
    private String fullName;
    private Integer wardId;
    private Integer districtId;
    private Integer provinceId;
    private String gender;
    private Double salary;
    private Integer level;
    private String type;
    private String content;
    private String address;
    private Boolean isDeleted;
    private String phone;
    private String email;
    private Integer jobType;
    private String jobName;
    private String companyName;
    private String countNumberJob;
    private Date deadline;
    private String search;
    private String title;
    private String position;
    private Double salaryStart;
    private Double salaryEnd;
    private Integer activityId;
    private Integer approve;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

}
