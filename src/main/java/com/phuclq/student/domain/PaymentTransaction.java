package com.phuclq.student.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID hợp đồng (pledge_contracts.id) */
    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    /** Mã hoặc loại thanh toán: LAI, GOC, PHI, CHUOC, GIAHAN */
    @Column(name = "transaction_type")
    private String transactionType;

    /** Số tiền khách trả */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /** Ngày giờ giao dịch */
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    /** Hình thức thanh toán: TIEN_MAT, CHUYEN_KHOAN */
    @Column(name = "payment_method")
    private String paymentMethod;

    /** Người thực hiện giao dịch (nhân viên) */
    @Column(name = "performed_by")
    private String performedBy;

    /** Ghi chú thêm */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
