package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER_HISTORY_HOME")
@Getter
@Setter
@Builder
@Table(name = "USER_HISTORY_HOME")
public class UserHistoryHome extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "USER_HISTORY_HOME_SEQUENCE", sequenceName = "USER_HISTORY_HOME_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_HISTORY_HOME_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "HOME_ID", nullable = false)
    private Long homeId;

    @Column(name = "ACTIVITY_ID", nullable = false)
    private Integer activityId;


}
