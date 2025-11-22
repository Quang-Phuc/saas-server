package com.phuclq.student.service.impl;

// src/main/java/com/example/lottery/admin/service/impl/VietlottResultServiceImpl.java
import com.phuclq.student.domain.VietlottResult;
import com.phuclq.student.repository.VietlottResultRepository;
import com.phuclq.student.service.VietlottResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VietlottResultServiceImpl implements VietlottResultService {

    private final VietlottResultRepository repo;

    public VietlottResultServiceImpl(VietlottResultRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<VietlottResult> findAll() {
        return repo.findAll();
    }

    @Override
    public VietlottResult findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Vietlott not found"));
    }

    @Override
    public VietlottResult create(VietlottResult r) {
        r.setId(null);
        return repo.save(r);
    }

    @Override
    public VietlottResult update(Long id, VietlottResult r) {
        VietlottResult ex = findById(id);
        r.setId(ex.getId());
        return repo.save(r);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
