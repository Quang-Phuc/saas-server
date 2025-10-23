package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class FileByCategoryDto {
    List<FileResultInterface> listFile;
    private Long id;
    private String category;

}
