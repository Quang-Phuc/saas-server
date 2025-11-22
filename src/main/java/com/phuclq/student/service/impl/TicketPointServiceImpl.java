package com.phuclq.student.service.impl;

// src/main/java/com/example/lottery/admin/service/impl/TicketPointServiceImpl.java
import com.phuclq.student.domain.TicketPoint;
import com.phuclq.student.repository.TicketPointRepository;
import com.phuclq.student.service.TicketPointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketPointServiceImpl implements TicketPointService {

    private final TicketPointRepository repo;

    public TicketPointServiceImpl(TicketPointRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<TicketPoint> findAll() {
        return repo.findAll();
    }

    @Override
    public TicketPoint findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Point not found"));
    }

    @Override
    public TicketPoint create(TicketPoint p) {
        p.setId(null);
        return repo.save(p);
    }

    @Override
    public TicketPoint update(Long id, TicketPoint p) {
        TicketPoint cur = repo.findById(id).orElseThrow();
        // copy field
        cur.setName(p.getName()); cur.setRegion(p.getRegion()); cur.setProvince(p.getProvince());
        cur.setDistrict(p.getDistrict()); cur.setAddress(p.getAddress()); cur.setHotline(p.getHotline());
        cur.setNote(p.getNote());
        cur.setHasXsmb(p.getHasXsmb()); cur.setHasVietlott(p.getHasVietlott()); cur.setHasQrPayment(p.getHasQrPayment());
        cur.setOpenTime(p.getOpenTime()); cur.setCloseTime(p.getCloseTime());
        cur.setLat(p.getLat()); cur.setLng(p.getLng());
        return repo.save(cur);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}

