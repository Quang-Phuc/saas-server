package com.phuclq.student.dto.blog;

import com.phuclq.student.dto.PaginationModel;
import lombok.Data;

import java.util.List;

@Data
public class BlogResultDto {
    List<BlogResult> list;
    PaginationModel paginationModel;

}
