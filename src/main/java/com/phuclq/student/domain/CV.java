package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CV")
@Getter
@Setter
@Builder
@Table(name = "CV")
public class CV extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "CV_SEQUENCE", sequenceName = "CV_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CV_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "WARD_ID", columnDefinition = "LONGTEXT")
    private Integer wardId;

    @Column(name = "DISTRICT_ID", nullable = false)
    private Integer districtId;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "SALARY")
    private Double salary;

    @Column(name = "SALARY_START")
    private Double salaryStart;

    @Column(name = "SALARY_END")
    private Double salaryEnd;

    @Column(name = "LEVEL")
    private Integer level;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "MONEY_TOP")
    private Double moneyTop;

    @Column(name = "START_MONEY_TOP")
    private LocalDateTime startMoneyTop;

    @Column(name = "END_MONEY_TOP")
    private LocalDateTime endMoneyTop;

    public CV(Integer loginId) {
        this.setLastUpdatedDate(LocalDateTime.now());
        this.setCreatedBy(loginId.toString());
        this.setCreatedDate(LocalDateTime.now());
        this.isDeleted = false;
    }
}
