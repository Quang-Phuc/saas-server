package com.phuclq.student.service;

// src/main/java/com/example/lottery/admin/service/LotteryDrawService.java

import com.phuclq.student.domain.LotteryDraw;

import java.util.List;

public interface LotteryDrawService {
    List<LotteryDraw> findAll();
    LotteryDraw findById(Long id);
    LotteryDraw create(LotteryDraw d);
    LotteryDraw update(Long id, LotteryDraw d);
    void delete(Long id);
}
