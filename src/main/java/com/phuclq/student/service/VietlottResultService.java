package com.phuclq.student.service;

// src/main/java/com/example/lottery/admin/service/VietlottResultService.java

import com.phuclq.student.domain.VietlottResult;

import java.util.List;

public interface VietlottResultService {
    List<VietlottResult> findAll();
    VietlottResult findById(Long id);
    VietlottResult create(VietlottResult r);
    VietlottResult update(Long id, VietlottResult r);
    void delete(Long id);
}
