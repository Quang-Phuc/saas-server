package com.phuclq.student.domain; // (Giả sử package của bạn)

import com.phuclq.student.types.InterestPaymentType;
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
@Table(name = "loans")
@Data
@EqualsAndHashCode(callSuper = true) // Cần thiết khi kế thừa từ Auditable
@NoArgsConstructor
@AllArgsConstructor
public class Loan extends Auditable<String> {

    /**
     * ID định danh (tự tăng) của khoản vay.
     * Sẽ được lưu ở cột 'loan_id' trong bảng 'pledge_contracts'.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId; // thuộc cửa hàng nào


    /**
     * Ngày khách hàng vay tiền (ngày bắt đầu hợp đồng).
     */
    @Column(name = "loan_date")
    private LocalDate loanDate;



    /**
     * Tổng số tiền cho vay (tiền gốc, đơn vị: VNĐ).
     */
    @Column(name = "loan_amount")
    private BigDecimal loanAmount;

    /**
     * Giá trị của kỳ đóng lãi (ví dụ: 1).
     * Đi kèm với 'interestTermUnit' (ví dụ: 1 Tháng).
     */
    @Column(name = "interest_term_value")
    private Integer interestTermValue;

    /**
     * Đơn vị của kỳ đóng lãi.
     * Ví dụ: "Ngay", "Tuan", "Thang".
     */
    @Column(name = "interest_term_unit")
    private InterestTermUnit  interestTermUnit;

    /**
     * Giá trị của lãi suất (ví dụ: 1.5).
     * Đi kèm với 'interestRateUnit'.
     */
    @Column(name = "interest_rate_value")
    private BigDecimal interestRateValue;

    /**
     * Đơn vị của lãi suất.
     * Ví dụ: "Lai/Trieu/Ngay", "Lai%/Thang".
     */
    @Column(name = "interest_rate_unit")
    private String interestRateUnit;

    /**
     * Tổng số lần (kỳ) khách hàng phải trả (cho cả gốc và lãi).
     */
    @Column(name = "payment_count")
    private Integer paymentCount;

    /**
     * Hình thức/Kiểu thu lãi.
     * Ví dụ: "Truoc" (Trả lãi định kỳ), "Sau" (Trả góp gốc + lãi).
     */
    @Column(name = "interest_payment_type")
    private InterestPaymentType interestPaymentType;

    /**
     * Ghi chú chung cho hợp đồng/khoản vay.
     */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;


    /**
     * Loại đối tác (thông tin phụ).
     * Ví dụ: "khach_hang", "chu_no".
     */
    @Column(name = "partner_type")
    private String partnerType;

    /**
     * ID của nhân viên/người theo dõi khoản vay này.
     * Dùng để JOIN qua bảng 'users' hoặc 'employees'.
     * (Kiểu String vì trong DTO có thể là 'all').
     */
    @Column(name = "follower_id")
    private String follower;

    /**
     * Nguồn khách hàng (thông tin phụ).
     * Ví dụ: "all", "ctv" (Cộng tác viên).
     */
    @Column(name = "customer_source")
    private String customerSource;

    /**
     * Ngày kết thúc hợp đồng (đến hạn chuộc hoặc đáo hạn).
     */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * Tổng số tiền lãi phải trả (cộng dồn, hệ thống có thể tính tự động).
     */
    @Column(name = "total_interest_amount")
    private BigDecimal totalInterestAmount;

    /**
     * Số tiền gốc còn lại (khi khách hàng trả góp hoặc trả dần).
     */
    @Column(name = "remaining_principal")
    private BigDecimal remainingPrincipal;

    /**
     * Phí lưu kho / bảo quản tài sản, tính riêng với tiền lãi.
     */
    @Column(name = "storage_fee")
    private BigDecimal storageFee;

    /**
     * Trạng thái hợp đồng (ví dụ: ACTIVE, CLOSED, RENEWED, OVERDUE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status;

    /**
     * ID khách hàng (liên kết tới bảng customers).
     */
    @Column(name = "customer_id")
    private Long customerId;

}