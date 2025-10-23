package com.phuclq.student.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@NoArgsConstructor
@Entity(name = "USER_HISTORY_COIN")
@Getter
@Setter
@Table(name = "USER_HISTORY_COIN")
public class UserHistoryCoin extends Auditable<String> {

    @Column(name = "CHECK_SUM")
    String checksum;
    @Id
    @SequenceGenerator(name = "USER_HISTORY_COIN_SEQUENCE", sequenceName = "USER_HISTORY_COIN_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_HISTORY_COIN_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "USER_ID", nullable = false)
    private Integer userId;
    @Column(name = "COIN", nullable = false)
    private Double coin;
    @Column(name = "TRANSACTION", nullable = false)
    private Integer transaction;
    @Column(name = "TRANSACTION_ID")
    private Integer transactionId;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "TYPE", nullable = false)
    private String type;
    @Column(name = "total_coin")
    private Double totalCoin;
    @Column(name = "PAYMENT_ORDER_ID")
    private Integer paymentOrderId;
    @Column(name = "STATUS")
    private String Status;
    @Column(name = "MRC_ORDER_ID")
    private String mrcOrderId;
    @Column(name = "PAYMENT_TIME")
    private Date paymentTime;
    @Column(name = "CALL_BACK_STATUS")
    private String callbackStatus;
    @Column
    private String txnId;
    @Column
    private String refNo;

    public UserHistoryCoin(Integer userId, Double coin, Integer transaction,
                           String description, Integer loginId, String type, Double totalCoin) {
        this.userId = userId;
        this.coin = coin;
        this.transaction = transaction;
        this.description = description;
        this.type = type;
        this.setCreatedBy(loginId.toString());
        this.setCreatedDate(LocalDateTime.now());
        this.totalCoin = totalCoin;

    }

    public UserHistoryCoin(Double coin, Integer transaction,
                           String description, Integer loginId, String type, Double totalCoin) {
        this.userId = loginId;
        this.coin = coin;
        this.transaction = transaction;
        this.description = description;
        this.type = type;
        this.setCreatedBy(loginId.toString());
        this.setCreatedDate(LocalDateTime.now());
        this.totalCoin = totalCoin;

    }
}
