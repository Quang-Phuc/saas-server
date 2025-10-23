package com.phuclq.student.dto.job;

import com.phuclq.student.dto.PaginationModel;
import lombok.Data;

import java.util.List;

@Data
public class HomeResultDto {
    List<HomeResult> list;
    PaginationModel paginationModel;

}
