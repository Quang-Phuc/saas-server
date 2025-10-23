package com.phuclq.student.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class FileHomeDoFilterDTO {
    List<FileResult> listFile;
    private BigInteger id;
    private String category;
}
