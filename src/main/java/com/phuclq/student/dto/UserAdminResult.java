package com.phuclq.student.dto;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class UserAdminResult {


    private Integer id;
    private String userName;
    private String email;
    private String phone;
    private Integer roleId;
    private Timestamp createdDate;
    private String address;
    private String fullName;
    private String gender;
    private String introduction;
    private Date birthDay;
    private BigInteger isUpload;
    private BigInteger isDownload;

    public UserAdminResult(Object[] obj) {
        this.id = (Integer) obj[0];
        this.userName = (String) obj[1];
        this.email = (String) obj[2];
        this.phone = (String) obj[3];
        this.roleId = (Integer) obj[4];
        this.createdDate = (Timestamp) obj[5];
        this.address = (String) obj[6];
        this.fullName = (String) obj[7];
        this.gender = (String) obj[8];
        this.introduction = (String) obj[9];
        this.birthDay = (Date) obj[10];

    }

    public UserAdminResult() {

    }

}
