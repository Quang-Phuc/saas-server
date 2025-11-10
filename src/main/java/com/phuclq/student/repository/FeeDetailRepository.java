package com.phuclq.student.repository;

// (Đặt trong package ...repository)
import com.phuclq.student.domain.CollateralAsset;
import com.phuclq.student.domain.FeeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeDetailRepository extends JpaRepository<FeeDetail, Long> {

    List<FeeDetail> findByContractId(Long contractId);

    void deleteByContractId(Long id);
}
