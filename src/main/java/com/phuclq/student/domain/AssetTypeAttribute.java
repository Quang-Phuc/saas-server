package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "asset_type_attribute")
public class AssetTypeAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @JoinColumn(name = "ASSET_TYPE_ID")
    private Long assetTypeId;
}
