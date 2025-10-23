package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "BLOG")
@Getter
@Setter
@Builder
@Table(name = "BLOG")
public class Blog extends Auditable<String> {


    @Id
    @SequenceGenerator(name = "BLOG_SEQUENCE", sequenceName = "BLOG_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLOG_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "category_blog_id", nullable = false)
    private Long categoryBlogId;

    @Column(name = "category_blog_name")
    private String categoryBlogName;

    @Column(name = "ID_URL", columnDefinition = "LONGTEXT")
    private String idUrl;

    @Column(name = "title", nullable = false, columnDefinition = "LONGTEXT")
    private String title;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "URL", columnDefinition = "LONGTEXT")
    private String url;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "delete_id")
    private Integer deleteId;

    @Column(name = "delete_date")
    private Timestamp deleteDate;

    @Column(name = "STATUS")
    private Boolean status;

    @Column(name = "reading")
    private Integer reading;

    @Column(name = "total_like")
    private Integer totalLike;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "total_comment")
    private Integer totalComment;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "ALT", columnDefinition = "LONGTEXT")
    private String alt;

}
