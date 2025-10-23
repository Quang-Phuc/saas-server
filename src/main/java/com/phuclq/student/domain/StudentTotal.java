package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "STUDENT_TOTAL")
@Getter
@Setter
@Builder
@Table(name = "STUDENT_TOTAL")
public class StudentTotal {
    @Id
    @SequenceGenerator(name = "STUDENT_TOTAL_SEQUENCE", sequenceName = "STUDENT_TOTAL_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STUDENT_TOTAL_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "DETAIL", nullable = false)
    private String detail;


}
