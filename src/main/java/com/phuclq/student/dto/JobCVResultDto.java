package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobCVResultDto {
    List<JobCVResult> list;
    PaginationModel paginationModel;

}
