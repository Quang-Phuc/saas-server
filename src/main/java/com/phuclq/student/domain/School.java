package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class School extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "SCHOOL_NAME")
    private String schoolName;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "WARD_ID", columnDefinition = "LONGTEXT")
    private Integer wardId;

    @Column(name = "DISTRICT_ID", nullable = false)
    private Integer districtId;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "SUMMARY", columnDefinition = "LONGTEXT")
    private String summary;

    @Column(name = "TOTAL_STUDENT")
    private Integer totalStudent;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "total_comment")
    private Integer totalComment;

    @Column(name = "total_rate")
    private Double totalRate;

    @Column(name = "SCHOOL_TYPE")
    private String schoolType;

    @Column(name = "SCHOOL_TYPE_EDUCATION")
    private String schoolTypeEducation;

    @Transient
    private String url;

    @Transient
    private String wardName;

    @Transient
    private String districtName;

    @Transient
    private String provinceName;

    @Transient
    private Double totalRateUser;


}
