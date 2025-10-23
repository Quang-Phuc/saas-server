package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER_HISTORY_BLOG")
@Getter
@Setter
@Builder
@Table(name = "USER_HISTORY_BLOG")
public class UserHistoryBlog extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "USER_HISTORY_BLOG_SEQUENCE", sequenceName = "USER_HISTORY_BLOG_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_HISTORY_BLOG_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "BLOG_ID", nullable = false)
    private Long blogId;

    @Column(name = "ACTIVITY_ID", nullable = false)
    private Integer activityId;


}
