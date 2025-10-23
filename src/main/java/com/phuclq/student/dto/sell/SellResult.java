package com.phuclq.student.dto.sell;


import lombok.Data;

import javax.persistence.Column;
import java.math.BigInteger;
import java.util.List;

@Data
public class SellResult {

    private BigInteger id;
    private String createBy;
    private String nameCategory;
    private String content;
    private Integer districtId;
    private Integer provinceId;
    private Integer wardId;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String title;
    private String url;
    private Boolean isLike;
    private Boolean isCard;
    private List<String> urls;
    private String name;
    private String createdDate;
    private Integer totalCard;
    private String idUrl;
    private String idUrlCategory;
    private Integer approverId;

    public SellResult(Object[] obj) {
        this.id = (BigInteger) obj[0];
        this.createBy = (String) obj[1];
        this.nameCategory = (String) obj[2];
        this.content = (String) obj[3];
        this.districtId = (Integer) obj[4];
        this.provinceId = (Integer) obj[5];
        this.provinceName = (String) obj[6];
        this.districtName = (String) obj[7];
        this.wardName = (String) obj[8];
        this.url = (String) obj[9];
        this.title = (String) obj[10];
        this.totalCard = (Integer) obj[11];
        this.createdDate = (String) obj[12];
        this.idUrl = (String) obj[13];
        this.idUrlCategory = (String) obj[14];
        this.approverId = (Integer) obj[15];
    }

}
