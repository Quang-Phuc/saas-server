package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_history")
public class UserHistory extends Auditable<String> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "Order_ID")
    private String orderId;

    @Column(name = "Transction_ID")
    private String transctionId;

    @Column(name = "Status")
    private Integer status;

    public UserHistory(Integer userId, Integer activityId) {
        this.userId = userId;
        this.activityId = activityId;
    }

    public UserHistory(int id, String orderId, String transctionId, Integer status) {
        this.id = id;
        this.orderId = orderId;
        this.transctionId = transctionId;
        this.status = status;
    }

    public UserHistory() {

    }
}
