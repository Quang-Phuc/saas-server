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

    @Column(name = "NAME", nullable = false, unique = true)
    private String name; // Tên loại tài sản

    @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
    private String description; // Mô tả chi tiết loại tài sản

    @Override
    public String toString() {
        return "AssetType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
