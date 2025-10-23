package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class HistoryFileResultResult {

    List<HistoryFileResultDto> historyFileResultDtos;
    PaginationModel paginationModel;
}
