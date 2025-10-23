package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Activity extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ACTIVITY", length = 1000)
    private String activity;

    @Column(name = "NAME", length = 1000)
    private String name;

}
