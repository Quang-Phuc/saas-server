package com.phuclq.student.repository;

import com.phuclq.student.domain.LicenseHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Long> {

    @Query(
            value = "SELECT lh.*, lp.name AS package_name, lp.price AS package_price, lp.created_date AS package_created_date " +
                    "FROM license_history lh " +
                    "LEFT JOIN license_package lp ON lh.license_package_id = lp.id " +
                    "WHERE (:keyword IS NULL OR :keyword = '' " +
                    "       OR LOWER(lh.action) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "       OR LOWER(lh.note) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "       OR LOWER(lp.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                    "ORDER BY lh.created_date DESC",
            countQuery = "SELECT COUNT(*) FROM license_history lh " +
                    "LEFT JOIN license_package lp ON lh.user_license_id = lp.id " +
                    "WHERE (:keyword IS NULL OR :keyword = '' " +
                    "       OR LOWER(lh.action) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "       OR LOWER(lh.note) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "       OR LOWER(lp.name) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true
    )
    Page<Object[]> searchLicenseHistories(String keyword, Pageable pageable);



    Page<LicenseHistory> findByUserId(Integer userId, Pageable pageable);

    Page<LicenseHistory> findByUserIdAndPackageNameContainingIgnoreCase(Integer userId, String packageName, Pageable pageable);

    Optional<LicenseHistory> findTopByUserIdOrderByActionDateDesc(Integer userId);
}
