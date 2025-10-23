package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private Long tokenid;

    @Column(name = "confirmation_token", columnDefinition = "LONGTEXT")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Integer userId;

    public ConfirmationToken(Integer userId) {
        this.userId = userId;
        createdDate = new Date();
        confirmationToken = "TOKENT_"+UUID.randomUUID().toString();
    }

    public ConfirmationToken() {

    }
}
