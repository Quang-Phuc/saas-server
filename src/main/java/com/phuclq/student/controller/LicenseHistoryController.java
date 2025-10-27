package com.phuclq.student.controller;

import com.phuclq.student.domain.LicenseHistory;
import com.phuclq.student.service.LicenseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/license-history")
@RequiredArgsConstructor
public class LicenseHistoryController {

    private final LicenseHistoryService licenseHistoryService;

    @PostMapping
    public LicenseHistory create(@RequestBody LicenseHistory licenseHistory) {
        return licenseHistoryService.create(licenseHistory);
    }

    @PutMapping("/{id}")
    public LicenseHistory update(@PathVariable Long id, @RequestBody LicenseHistory licenseHistory) {
        return licenseHistoryService.update(id, licenseHistory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        licenseHistoryService.delete(id);
    }

    @GetMapping
    public Page<Map<String, Object>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return licenseHistoryService.getAll(keyword, pageable);
    }

    @GetMapping("/{id}")
    public LicenseHistory getById(@PathVariable Long id) {
        return licenseHistoryService.getById(id);
    }
}
