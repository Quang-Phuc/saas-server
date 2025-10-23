package com.phuclq.student.dto.blog;

import com.phuclq.student.domain.Auditable;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlogDto  {


    private Long id;

    private Long categoryBlogId;

    private String categoryBlogName;

    private String idUrl;

    private String title;




}
