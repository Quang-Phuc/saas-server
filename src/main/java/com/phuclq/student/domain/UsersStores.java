package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "USERS_STORES")
@Data
public class UsersStores extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    @Column
    private Long storeId;

}
