package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "REPORT")
@Getter
@Setter
@Builder
@Table(name = "REPORT")
public class Report extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "REPORT_SEQUENCE", sequenceName = "REPORT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPORT_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "REQUEST_ID", nullable = false)
    private Integer requestId;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "URL", columnDefinition = "LONGTEXT")
    private String url;


}
