package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "JOB")
@Getter
@Setter
@Builder
@Table(name = "JOB")
public class Job extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "JOB_SEQUENCE", sequenceName = "JOB_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "JOB_TYPE", nullable = false)
    private Integer jobType;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "JOB_NAME", nullable = false)
    private String jobName;

    @Column(name = "COMPANY_NAME", columnDefinition = "LONGTEXT")
    private String companyName;

    @Column(name = "WARD_ID", columnDefinition = "LONGTEXT")
    private Integer wardId;

    @Column(name = "DISTRICT_ID", nullable = false)
    private Integer districtId;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "SALARY")
    private Double salary;

    @Column(name = "SALARY_START")
    private Double salaryStart;

    @Column(name = "SALARY_END")
    private Double salaryEnd;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "LEVEL")
    private Integer level;

    @Column(name = "title")
    private String title;

    @Column(name = "COUNT_NUMBER_JOB")
    private String countNumberJob;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DEAD_LINE")
    private Date deadline;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "MONEY_TOP")
    private Double moneyTop;

    @Column(name = "START_MONEY_TOP")
    private LocalDateTime startMoneyTop;

    @Column(name = "END_MONEY_TOP")
    private LocalDateTime endMoneyTop;

    public Job(Integer loginId) {
        this.setLastUpdatedDate(LocalDateTime.now());
        this.setCreatedBy(loginId.toString());
        this.setCreatedDate(LocalDateTime.now());
        this.isDeleted = false;
        this.approverId = null;
        this.approvedDate = null;
    }
}
