package com.phuclq.student.domain;

// src/main/java/com/example/lottery/admin/entity/VietlottResult.java
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "vietlott_results")
public class VietlottResult extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate drawDate;

    @Column(nullable = false, length = 32)
    private String game; // POWER_655, ...

    @Column(length = 32)
    private String code;

    @Column(nullable = false, length = 255)
    private String numbers;

    @Column(length = 255)
    private String extra;

    @Column(length = 64)
    private String jackpot;

    @Column(length = 512)
    private String note;



}

