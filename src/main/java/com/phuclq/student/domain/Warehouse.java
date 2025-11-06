package com.phuclq.student.domain;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "WAREHOUSE")
@Data
public class Warehouse extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
    private String description;

    /**
     * ID của cửa hàng/chi nhánh tạo ra hợp đồng này.
     * Dùng để phân biệt dữ liệu giữa các cửa hàng.
     */
    @Column( name = "store_id")
    private Long storeId;
}
