package com.phuclq.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserHistoryCoinResultDto {
    List<UserHistoryResult> list;
    PaginationModel paginationModel;

}
