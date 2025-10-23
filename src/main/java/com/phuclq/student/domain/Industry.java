package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Industry extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "value")
    private String value;


}
