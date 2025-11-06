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
@Table(name = "collateral_attribute")
@Data
@EqualsAndHashCode(callSuper = true) // Cần thiết khi kế thừa từ Auditable
@NoArgsConstructor
@AllArgsConstructor
public class CollateralAttribute extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collateral_asset_id", nullable = false)
    private Long collateralAssetId; // chỉ lưu ID, không ánh xạ object

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "value")
    private String value;

    @Column(name = "required")
    private Boolean required = false;

}
