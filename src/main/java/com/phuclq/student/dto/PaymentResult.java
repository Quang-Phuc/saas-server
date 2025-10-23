package com.phuclq.student.dto;

import lombok.Data;

@Data
public class PaymentResult {

    private String userName;
    private String email;
    private String phone;
    private String fullName;
    private Integer id;
    private String createdDate;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String bankShortName;
    private Double coin;
    private Integer status;
    private String imageQR;
    private String imagePayed;
    private String messages;

    public PaymentResult(Object[] obj) {
        this.userName = (String) obj[0];
        this.email = (String) obj[1];
        this.phone = (String) obj[2];
        this.fullName = (String) obj[3];
        this.id = (Integer) obj[4];
        this.createdDate = (String) obj[5];
        this.accountName = (String) obj[6];
        this.accountNumber = (String) obj[7];
        this.bankName = (String) obj[8];
        this.bankShortName = (String) obj[9];
        this.coin = (Double) obj[10];
        this.status = (Integer) obj[11];
        this.imageQR = (String) obj[12];
        this.imagePayed = (String) obj[13];
        this.messages = (String) obj[14];
    }

    public PaymentResult() {

    }

}
