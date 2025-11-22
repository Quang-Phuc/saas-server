package com.phuclq.student.controller.admin;

// src/main/java/com/example/lottery/admin/web/LotteryDrawController.java
import com.phuclq.student.domain.LotteryDraw;
import com.phuclq.student.service.LotteryDrawService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lottery")
@CrossOrigin(origins = "*")
public class LotteryDrawController {

    private final LotteryDrawService service;

    public LotteryDrawController(LotteryDrawService service) {
        this.service = service;
    }

    @GetMapping
    public List<LotteryDraw> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LotteryDraw get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public LotteryDraw create(@RequestBody LotteryDraw d) {
        return service.create(d);
    }

    @PutMapping("/{id}")
    public LotteryDraw update(@PathVariable Long id, @RequestBody LotteryDraw d) {
        return service.update(id, d);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
