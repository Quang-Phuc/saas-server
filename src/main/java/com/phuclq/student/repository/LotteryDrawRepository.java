package com.phuclq.student.repository;

// src/main/java/com/example/lottery/admin/repo/LotteryDrawRepository.java
import com.phuclq.student.domain.LotteryDraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryDrawRepository extends JpaRepository<LotteryDraw, Long> {
}

