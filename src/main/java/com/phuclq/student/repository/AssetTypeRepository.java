package com.phuclq.student.repository;

import com.phuclq.student.domain.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Integer> {

    @Query(
            value = "SELECT * FROM asset_type WHERE store_id IS NULL " +
                    "UNION ALL " +
                    "SELECT * FROM asset_type WHERE store_id = :storeId",
            nativeQuery = true
    )
    List<AssetType> findAllByStoreIdOrNull(Long storeId);

}
