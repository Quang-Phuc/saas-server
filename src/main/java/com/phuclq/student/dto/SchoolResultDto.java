package com.phuclq.student.dto;

import com.phuclq.student.domain.School;
import lombok.Data;

import java.util.List;

@Data
public class SchoolResultDto {
    List<School> list;
    PaginationModel paginationModel;

}
