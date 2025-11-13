package com.phuclq.student.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "payment_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSchedule extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID hợp đồng cầm cố (pledge_contracts.id) */
    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    /** Kỳ thứ mấy (ví dụ: 1, 2, 3...) */
    @Column(name = "period_number")
    private Integer periodNumber;

    /** Ngày đến hạn thanh toán */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /** Số tiền lãi phải trả kỳ này */
    @Column(name = "interest_amount")
    private BigDecimal interestAmount;

    /** Số tiền gốc phải trả kỳ này */
    @Column(name = "principal_amount")
    private BigDecimal principalAmount;

    /** Tổng tiền phải trả kỳ này */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    /** Trạng thái kỳ thanh toán: PENDING, PAID, OVERDUE */
    @Column(name = "status")
    private String status;

    /** Số tiền khách đã thanh toán (nếu có) */
    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    /** Ngày thực tế khách thanh toán (nếu có) */
    @Column(name = "paid_date")
    private LocalDate paidDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_schedule_id", insertable = false, updatable = false)
    private List<PaymentScheduleTransaction> transactions;

}
