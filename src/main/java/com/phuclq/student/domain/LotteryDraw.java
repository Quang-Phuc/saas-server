package com.phuclq.student.domain;

import com.phuclq.student.types.InterestPaymentType;
import com.phuclq.student.types.InterestRateUnit;
import com.phuclq.student.types.InterestTermUnit;
import com.phuclq.student.types.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Bảng này lưu trữ thông tin chi tiết về khoản vay/hợp đồng cầm cố.
 * Mỗi bản ghi ở đây sẽ được liên kết với một 'PledgeContract'
 * thông qua 'loan_id'.
 * Kế thừa từ 'Auditable' để có các trường created_date, last_modified_date, v.v.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true) // Cần thiết khi kế thừa từ Auditable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lottery_draws")
public class LotteryDraw extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MB / MN / MT
    @Column(nullable = false, length = 4)
    private String region;

    @Column(nullable = false, length = 128)
    private String province;

    @Column(nullable = false)
    private LocalDate drawDate;

    @Column(length = 16, nullable = false)
    private String gDB;

    @Column(length = 64)
    private String g1;

    @Column(length = 255)
    private String g2;

    @Column(length = 255)
    private String g3;

    @Column(length = 255)
    private String g4;

    @Column(length = 255)
    private String g5;

    @Column(length = 255)
    private String g6;

    @Column(length = 255)
    private String g7;


}
