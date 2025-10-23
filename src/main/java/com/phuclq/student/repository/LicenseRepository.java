package com.phuclq.student.repository;


import com.phuclq.student.domain.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
}

