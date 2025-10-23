package com.phuclq.student.dto;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class JobCVResult {

    private Double salaryEnd;
    private Double salaryStart;
    private BigInteger id;
    private String createBy;
    private Timestamp createdDate;
    private String fullName;
    private String wardId;
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
    private Timestamp deadline;
    private String search;
    private String title;
    private String provinceName;
    private String districtName;
    private String wardName;
    private Boolean isLike;
    private String url;
    private Integer approverId;
    private String position;

    public JobCVResult(Object[] obj) {
        this.id = (BigInteger) obj[0];
        this.createBy = (String) obj[1];
        this.createdDate = (Timestamp) obj[2];
        this.address = (String) obj[3];
        this.content = (String) obj[4];
        this.districtId = (Integer) obj[5];
        this.email = (String) obj[6];
        this.fullName = (String) obj[7];
        this.gender = (String) obj[8];
        this.level = (Integer) obj[9];
        this.phone = (String) obj[10];
        this.provinceId = (Integer) obj[11];
        this.salary = (Double) obj[12];
        this.salaryEnd = (Double) obj[13];
        this.salaryStart = (Double) obj[14];
        this.type = (String) obj[15];
        this.wardId = (String) obj[16];
        this.provinceName = (String) obj[17];
        this.districtName = (String) obj[18];
        this.wardName = (String) obj[19];
        this.url = (String) obj[20];
        this.approverId = (Integer) obj[21];
        this.position = (String) obj[22];

    }

    public JobCVResult() {

    }

}
