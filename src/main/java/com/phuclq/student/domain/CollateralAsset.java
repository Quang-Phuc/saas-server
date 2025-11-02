package com.phuclq.student.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    /**
     * Mã tài sản nội bộ (do cửa hàng tự định nghĩa, ví dụ: "XM-001").
     * Có thể dùng để tra cứu nhanh.
     */
    @Column(name = "asset_code")
    private String assetCode;

    /**
     * Biển kiểm soát (biển số xe) của tài sản (nếu là xe).
     */
    @Column(name = "license_plate")
    private String licensePlate;

    /**
     * Số khung của xe (nếu tài sản là xe).
     */
    @Column(name = "chassis_number")
    private String chassisNumber;

    /**
     * Số máy của xe (nếu tài sản là xe).
     */
    @Column(name = "engine_number")
    private String engineNumber;

    /**
     * Giá trị định giá của tài sản (đơn vị: VNĐ).
     * (Lưu ý: BigDecimal thường được khuyên dùng cho tiền tệ).
     */
    @Column(name = "valuation")
    private Long valuation;

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
}