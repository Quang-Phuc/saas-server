package com.phuclq.student.dto.blog;

import com.phuclq.student.domain.Blog;
import com.phuclq.student.dto.PaginationModel;
import lombok.Data;

import java.util.List;

@Data
public class BlogDetailDto {
    Blog blog;
    List<BlogDto> blogSeeMore;

}
