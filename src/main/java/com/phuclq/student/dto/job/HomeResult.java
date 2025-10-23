package com.phuclq.student.dto.job;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Data
public class HomeResult {

    private Double salaryEnd;
    private Double salaryStart;
    private BigInteger id;
    private String createBy;
    private String fullName;
    private String wardId;
    private Integer districtId;
    private Integer provinceId;
    private String gender;
    private Double price;
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
    private Boolean isCard;
    private String url;
    private List<String> urls;
    private String name;
    private String acreage;
    private Boolean airCondition;
    private Boolean fridge;
    private Boolean washingMachine;
    private Integer approverId;
    private Boolean shared;
    private String nameUser;
    private Boolean closed;
    private String createdDate;
    private String idUrl;

    public HomeResult(Object[] obj) {
        this.id = (BigInteger) obj[0];
        this.createBy = (String) obj[1];
        this.address = (String) obj[2];
        this.content = (String) obj[3];
        this.districtId = (Integer) obj[4];
        this.email = (String) obj[5];
        this.isDeleted = (Boolean) obj[6];
        this.phone = (String) obj[7];
        this.provinceId = (Integer) obj[8];
        this.price = (Double) obj[9];
        this.wardId = (String) obj[10];
        this.provinceName = (String) obj[11];
        this.districtName = (String) obj[12];
        this.wardName = (String) obj[13];
        this.url = (String) obj[14];
        this.title = (String) obj[15];
        this.name = (String) obj[16];
        this.acreage = (String) obj[17];
        this.airCondition = (Boolean) obj[18];
        this.washingMachine = (Boolean) obj[19];
        this.fridge = (Boolean) obj[20];
        this.approverId = (Integer) obj[21];
        this.shared = (Boolean) obj[22];
        this.nameUser = (String) obj[23];
        this.closed = (Boolean) obj[24];
        this.createdDate = (String) obj[25];
        this.idUrl = (String) obj[26];

    }

    public HomeResult() {

    }

}
