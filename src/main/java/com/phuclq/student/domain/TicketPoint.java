package com.phuclq.student.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ticket_points")
@Data
public class TicketPoint extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MB / MN / MT
    @Column(length = 4, nullable = false)
    private String region;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false, length = 128)
    private String province;

    @Column(length = 128)
    private String district;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 32)
    private String hotline;

    @Column(length = 512)
    private String note;

    private Double lat;
    private Double lng;

    // services
    private Boolean hasXsmb;
    private Boolean hasVietlott;
    private Boolean hasQrPayment;

    // hours
    @Column(length=5) private String openTime;   // "08:00"
    @Column(length=5) private String closeTime;  // "22:00"


}

