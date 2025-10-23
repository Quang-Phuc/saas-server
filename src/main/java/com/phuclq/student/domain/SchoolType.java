package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SchoolType extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name", columnDefinition = "LONGTEXT")
    private String name;

    @Column(name = "TYPE")
    private String type;


}
