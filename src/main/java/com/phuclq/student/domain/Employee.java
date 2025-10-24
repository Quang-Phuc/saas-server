package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@Data
public class Employee extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId; // thuộc cửa hàng nào

    @Column(name = "user_id", nullable = false)
    private Long userId; // là user nào

    @Column(nullable = false)
    private String role; // ví dụ: STAFF, MANAGER
}
