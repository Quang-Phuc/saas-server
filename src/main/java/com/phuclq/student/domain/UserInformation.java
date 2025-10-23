package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user_information")
public class UserInformation extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "birth_date")
    private Timestamp birthDate;

    @Column(name = "gender")
    private Short gender;

    @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private Integer address;

    @Column(name = "specialized")
    private Integer specialized;

    @Column(name = "yourself")
    private String yourself;


}
