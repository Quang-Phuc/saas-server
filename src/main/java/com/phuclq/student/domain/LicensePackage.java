package com.phuclq.student.domain;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "license_package")
public class LicensePackage extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Column(name = "max_store")
    private Integer maxStore; // số lượng cửa hàng tối đa được tạo

    @Column(name = "max_user_per_store")
    private Integer maxUserPerStore; // số lượng user tối đa mỗi cửa hàng

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "discount")
    private Double discount; // phần trăm giảm giá (VD: 10 = 10%)

    @Column(name = "duration_days")
    private Integer durationDays; // số ngày sử dụng gói
}
