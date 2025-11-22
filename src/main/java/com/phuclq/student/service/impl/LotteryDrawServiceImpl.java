package com.phuclq.student.service.impl;

// src/main/java/com/example/lottery/admin/service/impl/LotteryDrawServiceImpl.java
import com.phuclq.student.domain.LotteryDraw;
import com.phuclq.student.repository.LotteryDrawRepository;
import com.phuclq.student.service.LotteryDrawService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LotteryDrawServiceImpl implements LotteryDrawService {

    private final LotteryDrawRepository repo;

    public LotteryDrawServiceImpl(LotteryDrawRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<LotteryDraw> findAll() {
        return repo.findAll();
    }

    @Override
    public LotteryDraw findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Draw not found"));
    }

    @Override
    public LotteryDraw create(LotteryDraw d) {
        d.setId(null);
        return repo.save(d);
    }

    @Override
    public LotteryDraw update(Long id, LotteryDraw d) {
        LotteryDraw ex = findById(id);
        d.setId(ex.getId());
        return repo.save(d);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
