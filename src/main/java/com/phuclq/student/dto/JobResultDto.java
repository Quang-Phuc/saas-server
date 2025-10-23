package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobResultDto {
    List<JobResult> list;
    PaginationModel paginationModel;

}
