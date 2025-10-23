package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class FileResultDto {
    List<FileResult> list;
    PaginationModel paginationModel;

}
