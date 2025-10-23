package com.phuclq.student.dto;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class JobResult {

    private Double salaryEnd;
    private Double salaryStart;
    private BigInteger id;
    private String createBy;
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

    public JobResult(Object[] obj) {
        this.id = (BigInteger) obj[0];
        this.createBy = (String) obj[1];
        this.address = (String) obj[2];
        this.companyName = (String) obj[3];
        this.content = (String) obj[4];
        this.countNumberJob = (String) obj[5];
        this.deadline = (Timestamp) obj[6];
        this.districtId = (Integer) obj[7];
        this.email = (String) obj[8];
        this.isDeleted = (Boolean) obj[9];
        this.jobName = (String) obj[10];
        this.jobType = (Integer) obj[11];
        this.level = (Integer) obj[12];
        this.phone = (String) obj[13];
        this.provinceId = (Integer) obj[14];
        this.salary = (Double) obj[15];
        this.salaryEnd = (Double) obj[16];
        this.salaryStart = (Double) obj[17];
        this.type = (String) obj[18];
        this.wardId = (String) obj[19];
        this.provinceName = (String) obj[20];
        this.districtName = (String) obj[21];
        this.wardName = (String) obj[22];
        this.url = (String) obj[23];
        this.approverId = (Integer) obj[24];
    }

    public JobResult() {

    }

}
