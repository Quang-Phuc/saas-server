package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "COMMENT")
@Getter
@Setter
@Builder
@Table(name = "COMMENT")
public class Comment extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "COMMENT_SEQUENCE", sequenceName = "COMMENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQUENCE")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "IMAGE_USER")
    private String imageUser;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "totalLike")
    private Integer totalLike;

    @Column(name = "IS_DELETE")
    private Boolean isDelete;

    @Column(name = "IS_ANONYMOUS")
    private Boolean isAnonymous;

    @Transient
    private Boolean isLike;


}
