package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "license_history")
@Data
public class LicenseHistory extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_license_id", nullable = false)
    private Long userLicenseId;

    @Column(length = 30, nullable = false)
    private String action; // PURCHASE, RENEW, EXPIRE, DOWNGRADE

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;

    @Column
    private String note;
}
