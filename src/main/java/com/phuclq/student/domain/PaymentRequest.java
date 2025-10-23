package com.phuclq.student.domain;

import com.phuclq.student.types.PaymentType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "PAYMENT_REQUEST")
@Entity(name = "PAYMENT_REQUEST")
@Data
public class PaymentRequest extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "PAYMENT_REQUEST_SEQUENCE", sequenceName = "PAYMENT_REQUEST_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_REQUEST_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "bank_logo")
    private String bankLogo;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_short_name")
    private String bankShortName;

    @Column(name = "name")
    private String name;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "coin")
    private Double coin;

    @Column(name = "IMAGE_QR")
    private String imageQR;

    @Column(name = "IMAGE_PAYED")
    private String imagePayed;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "messages", length = 1000)
    private String messages;

    @Column(name = "PAYMENT_DONE_DATE")
    private LocalDateTime paymentDoneDate;

    public PaymentRequest(Integer loginId) {
        this.setLastUpdatedDate(LocalDateTime.now());
        this.setCreatedBy(loginId.toString());
        this.setCreatedDate(LocalDateTime.now());
        this.setStatus(PaymentType.PAYMENT_PROCESSING.getCode());
    }


}
