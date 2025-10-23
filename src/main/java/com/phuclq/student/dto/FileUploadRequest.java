package com.phuclq.student.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileUploadRequest {
    List<RequestFileDTO> files;
    private Integer id;
    private String title;
    private String name;
    private Long categoryId;
    private Integer specializationId;
    private Integer industryId;
    private Integer languageId;
    private Integer schoolId;
    private Double filePrice;
    private Integer startPageNumber;
    private Integer endPageNumber;
    private String description;
    private Boolean isVip;
    private MultipartFile fileUpload;
    private MultipartFile fileDemo;
    private MultipartFile fileImage;


}
