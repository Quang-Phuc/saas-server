package com.phuclq.student.controller.admin;

// src/main/java/com/example/lottery/admin/web/TicketPointController.java
import com.phuclq.student.domain.TicketPoint;
import com.phuclq.student.service.TicketPointService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/points")
@CrossOrigin(origins = "*")
public class TicketPointController {

    private final TicketPointService service;

    public TicketPointController(TicketPointService service) {
        this.service = service;
    }

    @GetMapping
    public List<TicketPoint> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TicketPoint get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public TicketPoint create(@RequestBody TicketPoint p) {
        return service.create(p);
    }

    @PutMapping("/{id}")
    public TicketPoint update(@PathVariable Long id, @RequestBody TicketPoint p) {
        return service.update(id, p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
