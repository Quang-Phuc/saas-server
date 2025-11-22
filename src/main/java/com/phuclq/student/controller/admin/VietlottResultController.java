package com.phuclq.student.controller.admin;

// src/main/java/com/example/lottery/admin/web/VietlottResultController.java
import com.phuclq.student.domain.VietlottResult;
import com.phuclq.student.service.VietlottResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vietlott")
@CrossOrigin(origins = "*")
public class VietlottResultController {

    private final VietlottResultService service;

    public VietlottResultController(VietlottResultService service) {
        this.service = service;
    }

    @GetMapping
    public List<VietlottResult> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public VietlottResult get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public VietlottResult create(@RequestBody VietlottResult r) {
        return service.create(r);
    }

    @PutMapping("/{id}")
    public VietlottResult update(@PathVariable Long id, @RequestBody VietlottResult r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
