package com.phuclq.student.dto;


import lombok.Data;

import java.util.List;


@Data
public class PaymentRequestDto {


    List<RequestFileDTO> files;
    private Integer id;
    private String bankId;
    private String bankLogo;
    private String bankName;
    private String bankShortName;
    private String name;
    private String accountNumber;
    private String accountName;
    private Double coin;
    private String password;
    private String message;
    private Integer status;

}
