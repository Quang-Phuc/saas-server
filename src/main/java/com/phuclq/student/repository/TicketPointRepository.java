package com.phuclq.student.repository;

// src/main/java/com/example/lottery/admin/repo/TicketPointRepository.java

import com.phuclq.student.domain.TicketPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPointRepository extends JpaRepository<TicketPoint, Long> {
}
