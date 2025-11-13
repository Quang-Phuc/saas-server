package com.phuclq.student.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_schedule_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentScheduleTransaction extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID kỳ thanh toán (payment_schedule.id) */
    @Column(name = "payment_schedule_id", nullable = false)
    private Long paymentScheduleId;

    /** Số tiền khách thanh toán trong giao dịch này */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /** Ngày thực tế khách thanh toán */
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    /** Loại giao dịch (LÃI, GỐC, PHÍ, KHÁC...) */
    @Column(name = "type")
    private String type;

    /** Ghi chú giao dịch */
    @Column(name = "note")
    private String note;


}
