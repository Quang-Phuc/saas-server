package com.phuclq.student.repository;

import com.phuclq.student.domain.LicensePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicensePackageRepository extends JpaRepository<LicensePackage, Long> {


    Optional<LicensePackage> findByName(String name);
}
