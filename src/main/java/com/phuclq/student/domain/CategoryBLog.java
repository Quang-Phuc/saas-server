package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CATEGORY_BLOG")
@Getter
@Setter
@Builder
@Table(name = "CATEGORY_BLOG")
public class CategoryBLog extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", columnDefinition = "LONGTEXT")
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
    private String description;


}
