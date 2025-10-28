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

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    @Column
    private String note;

    @Column(name = "license_package_id", nullable = false)
    private Long licensePackageId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "package_price")
    private Double packagePrice;

    @Column(name = "package_discount")
    private Double packageDiscount;

    @Column(name = "package_duration_days")
    private Integer packageDurationDays;


}
