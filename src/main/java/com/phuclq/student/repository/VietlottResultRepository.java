package com.phuclq.student.repository;

// src/main/java/com/example/lottery/admin/repo/VietlottResultRepository.java


import com.phuclq.student.domain.VietlottResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VietlottResultRepository extends JpaRepository<VietlottResult, Long> {
}
