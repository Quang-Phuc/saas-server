package com.phuclq.student.dto.school;

import com.phuclq.student.dto.RequestFileDTO;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class SchoolRequest {

    List<RequestFileDTO> files;
    private Long id;
    private String schoolName;
    private String content;
    private String type;
    private Integer wardId;
    private Integer districtId;
    private Integer provinceId;
    private String address;
    private String summary;
    private Integer totalStudent;
    private String schoolType;
    private String search;
    private String schoolTypeEducation;
    private Integer orderType;
    private String order;
    private Integer page;
    private Integer size;

}
