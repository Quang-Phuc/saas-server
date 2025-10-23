package com.phuclq.student.dto.blog;


import lombok.Data;

import java.math.BigInteger;

@Data
public class BlogResult {

    private BigInteger id;

    private String title;
    private String categoryBlogName;

    private String content;

    private String createdDate;

    private Integer reading;

    private Integer totalLike;
    private String name;
    private String email;
    private String userName;
    private String fullName;
    private String url;
    private Boolean isLike;
    private String idUrl;
    private String idUrlCategory;
    private Integer approverId;
    private String description;
    private BigInteger categoryBlogId;
    private String alt;


    public BlogResult(Object[] obj) {

        this.id = (BigInteger) obj[0];
        this.title = (String) obj[1];
        this.content = (String) obj[2];
        this.createdDate = (String) obj[3];
        this.categoryBlogName = (String) obj[4];
        this.reading = (Integer) obj[5];
        this.totalLike = (Integer) obj[6];
        this.email = (String) obj[7];
        this.userName = (String) obj[8];
        this.fullName = (String) obj[9];
        this.url = (String) obj[10];
        this.idUrl = (String) obj[11];
        this.idUrlCategory = (String) obj[12];
        this.approverId = (Integer) obj[13];
        this.description = (String) obj[14];
        this.categoryBlogId = (BigInteger) obj[15];
        this.alt = (String) obj[16];
    }

}
