package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "asset_type")
public class AssetType extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TYPE_CODE")
    private String typeCode; // Tên loại tài sản

    @Column(name = "TYPE_NAME", columnDefinition = "LONGTEXT")
    private String typeName; // Mô tả chi tiết loại tài sản

    /**
     * ID của cửa hàng/chi nhánh tạo ra hợp đồng này.
     * Dùng để phân biệt dữ liệu giữa các cửa hàng.
     */
    @Column( name = "store_id")
    private Long storeId;

    @Column( name = "status")
    private String status;

    @Override
    public String toString() {
        return "AssetType{" +
                "id=" + id +
                ", name='" + typeCode + '\'' +
                ", description='" + typeName + '\'' +
                '}';
    }
}
