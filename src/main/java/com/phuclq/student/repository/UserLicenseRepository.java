package com.phuclq.student.repository;

import com.phuclq.student.domain.UserLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLicenseRepository extends JpaRepository<UserLicense, Long> {

    List<UserLicense> findByUserIdAndStatusOrderByIdDesc(Long userId);

    UserLicense findTopByUserIdOrderByPurchaseDateDesc(Long userId);
}
