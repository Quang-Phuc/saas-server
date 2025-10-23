package com.phuclq.student.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Set;

@Data
public class FileMyMapResult {
    Set<Integer> rowNums;
    private BigInteger categoryId;
    private String category;
}
