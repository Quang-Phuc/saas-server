package com.phuclq.student.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class FileMyResult {
    private String createBy;
    private Double rowNum;
    private BigInteger  categoryId;
    private String category;

    public FileMyResult(Object[] obj) {
        this.createBy = (String) obj[0];
        this.rowNum = (Double) obj[1];
        this.categoryId = (BigInteger) obj[2];
        this.category = (String) obj[3];

    }

    public FileMyResult() {

    }

}
