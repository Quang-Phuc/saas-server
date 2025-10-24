package com.phuclq.student.repository;

import com.phuclq.student.domain.LicenseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Long> {

    List<LicenseHistory> findByUserLicenseId(Long userLicenseId);
}
