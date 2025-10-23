package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CONTENT")
@Getter
@Setter
@Builder
@Table(name = "CONTENT")
public class Content extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "CONTENT_SEQUENCE", sequenceName = "CONTENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTENT_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name = "TYPE")
    private String type;


}
