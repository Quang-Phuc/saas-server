package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER_HISTORY_JOB")
@Getter
@Setter
@Builder
@Table(name = "USER_HISTORY_JOB")
public class UserHistoryJob extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "USER_HISTORY_JOB_SEQUENCE", sequenceName = "USER_HISTORY_JOB_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_HISTORY_JOB_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "JOB_ID", nullable = false)
    private Long jobId;

    @Column(name = "ACTIVITY_ID", nullable = false)
    private Integer activityId;

    @Column(name = "TYPE", nullable = false)
    private String type;


}
