package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "JOB_TYPE")
@Getter
@Setter
@Builder
@Table(name = "JOB_TYPE")
public class JobType extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "JOB_TYPE_SEQUENCE", sequenceName = "JOB_TYPE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_TYPE_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    public JobType(String name, Integer createBy) {
        this.name = name;
        this.setCreatedBy(createBy.toString());
        this.setCreatedDate(LocalDateTime.now());
    }
}
