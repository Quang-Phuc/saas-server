package com.phuclq.student.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Integer fileId;
    private String imageUser;
    private String userName;
    private String content;
    private String type;
    private Integer totalLike;
    private LocalDateTime createdDate;


}
