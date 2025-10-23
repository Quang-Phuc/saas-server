package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "LIKE_COMMENT")
@Getter
@Setter
@Builder
@Table(name = "LIKE_COMMENT")
public class LikeComment extends Auditable<String> {


    @Id
    @SequenceGenerator(name = "LIKE_COMMENT_SEQUENCE", sequenceName = "LIKE_COMMENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIKE_COMMENT_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "COMMENT_ID", nullable = false)
    private Long commentId;

    @Column(name = "COMMENT_TYPE")
    private String commentType;



}
