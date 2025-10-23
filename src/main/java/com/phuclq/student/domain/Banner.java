package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "BANNER")
@Getter
@Setter
@Builder
@Table(name = "BANNER")
public class Banner extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "BANNER_SEQUENCE", sequenceName = "BANNER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANNER_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name = "TYPE", columnDefinition = "LONGTEXT")
    private String type;


}
