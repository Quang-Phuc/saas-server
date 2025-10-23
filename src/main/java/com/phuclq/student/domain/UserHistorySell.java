package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER_HISTORY_SELL")
@Getter
@Setter
@Builder
@Table(name = "USER_HISTORY_SELL")
public class UserHistorySell extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "USER_HISTORY_SELL_SEQUENCE", sequenceName = "USER_HISTORY_SELL_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_HISTORY_SELL_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "SELL_ID", nullable = false)
    private Long sellId;

    @Column(name = "ACTIVITY_ID", nullable = false)
    private Integer activityId;


}
