package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "USER_COIN_BACKUP")
public class UserCoinBackup extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_coin")
    private Double totalCoin;

    @Column(name = "coin_compare")
    private Double totalCompare;

    public UserCoinBackup(Integer userId, Double totalCoin) {
        this.userId = userId;
        this.totalCoin = totalCoin;
    }


}
