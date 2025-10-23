package com.phuclq.student.dto.content;

import com.phuclq.student.domain.Content;
import com.phuclq.student.dto.PaginationModel;
import lombok.Data;

import java.util.List;

@Data
public class ContentResultDto {
    List<Content> list;
    PaginationModel paginationModel;

}
