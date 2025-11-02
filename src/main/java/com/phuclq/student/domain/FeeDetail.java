package com.phuclq.student.domain; // (Giả sử cùng package)

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Bảng này lưu trữ CHI TIẾT từng khoản phí (theo chiều dọc).
 * Mỗi hợp đồng sẽ có NHIỀU dòng trong bảng này (ví dụ: 1 dòng cho phí kho,
 * 1 dòng cho phí lưu kho, v.v.).
 */
@Entity
@Table(name = "fee_details") // Đặt tên mới là 'fee_details'
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeDetail extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ID của hợp đồng (PledgeContract) mà khoản phí này thuộc về.
     * Dùng để JOIN qua bảng 'pledge_contracts'.
     */
    @Column(name = "contract_id")
    private Long contractId; // (Kiểu String để khớp với ID (UUID) của PledgeContract)

    /**
     * Tên/Loại của khoản phí này.
     * Ví dụ: "warehouseFee", "storageFee", "riskFee", "managementFee".
     */
    @Column(name = "fee_type")
    private String feeType;

    /**
     * Loại giá trị, xác định cách tính của 'value'.
     * Ví dụ: "NhapTien" (giá trị là số tiền cố định)
     * hoặc "NhapPhanTram" (giá trị là tỷ lệ phần trăm).
     */
    @Column(name = "value_type")
    private String valueType;

    /**
     * Giá trị của khoản phí.
     * Có thể là một số tiền cụ thể (VNĐ) hoặc một giá trị phần trăm.
     */
    @Column(name = "value")
    private BigDecimal value;
}