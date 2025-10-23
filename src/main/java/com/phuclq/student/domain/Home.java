package com.phuclq.student.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Entity(name = "HOME")
@Getter
@Setter
@Builder
@Table(name = "HOME")
public class Home extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "HOME_SEQUENCE", sequenceName = "HOME_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HOME_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "WARD_ID", columnDefinition = "LONGTEXT")
    private Integer wardId;

    @Column(name = "DISTRICT_ID", nullable = false)
    private Integer districtId;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "ACREAGE")
    private String acreage;

    @Column(name = "CLOSED")
    private Boolean closed;

    @Column(name = "SHARED")
    private Boolean shared;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "DELETED_ID")
    private Integer deletedId;

    @Column(name = "delete_date")
    private Timestamp deleteDate;

    @Column(name = "AIR_CONDITION")
    private Boolean airCondition;

    @Column(name = "FRIDGE")
    private Boolean fridge;

    @Column(name = "WASHING_MACHINE")
    private Boolean washingMachine;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "name")
    private String name;

    @Column(name = "NAME_USER")
    private String nameUser;

    @Column(name = "MONEY_TOP")
    private Double moneyTop;

    @Column(name = "START_MONEY_TOP")
    private LocalDateTime startMoneyTop;

    @Column(name = "END_MONEY_TOP")
    private LocalDateTime endMoneyTop;

    public Home() {
        this.isDeleted = false;
    }
}
