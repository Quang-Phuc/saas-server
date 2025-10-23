package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportResult {
    List<ReportDTO> list;
    PaginationModel paginationModel;

}
