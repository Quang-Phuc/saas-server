package com.phuclq.student.domain;

import lombok.*;
import javax.persistence.*;

/**
 * Bảng chính (Root/Master Table) đại diện cho một Hợp đồng Cầm cố (Pledge Contract).
 * Bảng này chứa các ID liên kết đến các bảng thông tin chi tiết khác (Customer, Loan, Collateral).
 * Kế thừa từ 'Auditable' để có các trường created_date, last_modified_date, v.v.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "pledge_contracts")
@Data
@EqualsAndHashCode(callSuper = true)
public class PledgeContract extends Auditable<String> {

    /**
     * ID định danh (tự tăng) của Hợp đồng.
     * Lưu ý: Kiểu dữ liệu là Long, không phải Integer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ID của cửa hàng/chi nhánh tạo ra hợp đồng này.
     * Dùng để phân biệt dữ liệu giữa các cửa hàng.
     */
    @Column(nullable = false, name = "store_id")
    private String storeId;

    /**
     * Khóa ngoại (Foreign Key) liên kết đến ID của khách hàng.
     * Dùng để JOIN qua bảng 'customers'.
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * Khóa ngoại (Foreign Key) liên kết đến ID của khoản vay.
     * Dùng để JOIN qua bảng 'loans'.
     */
    @Column(name = "loan_id")
    private Long loanId;

    /**
     * Khóa ngoại (Foreign Key) liên kết đến ID của tài sản thế chấp.
     * Dùng để JOIN qua bảng 'collateral_asset'.
     */
    @Column(name = "collateral_id")
    private Long collateralId;
}