package com.phuclq.student.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Entity(name = "SELL")
@Getter
@Setter
@Builder
@Table(name = "SELL")
public class Sell extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;


    @Column(name = "SELL_CATEGORY_ID", nullable = false)
    private Long sellCategoryId;

    @Column(name = "WARD_ID", columnDefinition = "LONGTEXT")
    private Integer wardId;

    @Column(name = "DISTRICT_ID", nullable = false)
    private Integer districtId;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "MONEY_TOP")
    private Double moneyTop;

    @Column(name = "START_MONEY_TOP")
    private LocalDateTime startMoneyTop;

    @Column(name = "END_MONEY_TOP")
    private LocalDateTime endMoneyTop;

    @Column(name = "TOTAL_CARD")
    private Integer totalCard;

    @Column(name = "delete_id")
    private Integer deleteId;

    @Column(name = "delete_date")
    private Timestamp deleteDate;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "QUANTITY")
    private Double quantity;



    public Sell() {
        this.isDeleted = false;
    }
}
