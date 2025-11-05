package com.phuclq.student.repository;

import com.phuclq.student.domain.AssetTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetTypeAttributeRepository extends JpaRepository<AssetTypeAttribute, Integer> {
    List<AssetTypeAttribute> findByAssetTypeId(Long assetTypeId);

    void deleteByAssetTypeId(Long assetTypeId);

    @Query(
            value = "SELECT * FROM asset_type_attribute WHERE asset_type_id IN (:assetTypeIds)",
            nativeQuery = true
    )
    List<AssetTypeAttribute> findByAssetTypeIds(List<Long> assetTypeIds);

}
