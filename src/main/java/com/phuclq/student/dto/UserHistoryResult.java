package com.phuclq.student.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class UserHistoryResult {


    private Integer id;
    private Timestamp createdDate;
    private Double coin;
    private String description;
    private Integer transaction;
    private String type;
    private String userName;
    private String email;
    private String phone;
    private String address;
    private String fullName;
    private String gender;
    private String introduction;
    private Date birthDay;
    private Double totalCoin;

    public UserHistoryResult(Object[] obj) {
        this.id = (Integer) obj[0];
        this.createdDate = (Timestamp) obj[1];
        this.coin = (Double) obj[2];
        this.description = (String) obj[3];
        this.transaction = (Integer) obj[4];
        this.type = (String) obj[5];
        this.userName = (String) obj[6];
        this.email = (String) obj[7];
        this.phone = (String) obj[8];
        this.address = (String) obj[9];
        this.fullName = (String) obj[10];
        this.gender = (String) obj[11];
        this.introduction = (String) obj[12];
        this.birthDay = (Date) obj[13];
        this.totalCoin = (Double) obj[14];

    }

    public UserHistoryResult() {

    }

}
