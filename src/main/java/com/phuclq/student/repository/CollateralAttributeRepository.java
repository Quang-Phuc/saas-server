package com.phuclq.student.repository;


import com.phuclq.student.domain.CollateralAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollateralAttributeRepository extends JpaRepository<CollateralAttribute, Long> {

    // Lấy danh sách attribute theo collateralAssetId
    List<CollateralAttribute> findByCollateralAssetId(Long collateralAssetId);

    // Xóa theo collateralAssetId (nếu cần xoá khi update asset)
    void deleteByCollateralAssetId(Long collateralAssetId);
}
