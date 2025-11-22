package com.phuclq.student.service;

// src/main/java/com/example/lottery/admin/service/TicketPointService.java

import com.phuclq.student.domain.TicketPoint;

import java.util.List;

public interface TicketPointService {
    List<TicketPoint> findAll();
    TicketPoint findById(Long id);
    TicketPoint create(TicketPoint p);
    TicketPoint update(Long id, TicketPoint p);
    void delete(Long id);
}

