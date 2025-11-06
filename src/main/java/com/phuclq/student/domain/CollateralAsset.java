package com.phuclq.student.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Bảng này lưu trữ thông tin chi tiết về các tài sản thế chấp (ví dụ: xe máy, laptop)
 * liên quan đến một hợp đồng.
 * Kế thừa từ 'Auditable' để có các trường created_date, last_modified_date, v.v.
 */
@Entity
@Table(name = "collateral_asset")
@Data
@EqualsAndHashCode(callSuper = true) // Cần thiết khi kế thừa từ Auditable
@NoArgsConstructor
@AllArgsConstructor
public class CollateralAsset extends Auditable<String> {

    /**
     * ID định danh (tự tăng) của tài sản thế chấp.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "asset_type")
    private Long assetType;

    /**
     * Giá trị định giá của tài sản (đơn vị: VNĐ).
     * (Lưu ý: BigDecimal thường được khuyên dùng cho tiền tệ).
     */
    @Column(name = "valuation")
    private BigDecimal valuation;

    /**
     * Ghi chú chi tiết về tình trạng hoặc thông tin khác của tài sản.
     */
    @Column(name = "asset_note", columnDefinition = "TEXT")
    private String assetNote;

    /**
     * ID của kho bãi nơi tài sản đang được lưu trữ.
     * Dùng để JOIN qua bảng 'warehouses'.
     */
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * ID của hợp đồng (PledgeContract) mà tài sản này được thế chấp.
     * Dùng để JOIN qua bảng 'pledge_contracts'.
     */
    @Column(name = "contract_id")
    private Long contractId;

    /**
     * Trạng thái của tài sản (ví dụ: "TrongKho", "DaThanhLy", "DaTraKhach").
     */
    @Column(name = "status")
    private String status;

    /**
     * ID của cửa hàng/chi nhánh tạo ra hợp đồng này.
     * Dùng để phân biệt dữ liệu giữa các cửa hàng.
     */
    @Column(nullable = false, name = "store_id")
    private Long storeId;
}