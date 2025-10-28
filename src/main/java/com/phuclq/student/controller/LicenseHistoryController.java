package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.LicenseHistory;
import com.phuclq.student.service.LicenseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/license-history")
@RequiredArgsConstructor
public class LicenseHistoryController {

    private final LicenseHistoryService licenseHistoryService;
    private final RestEntityResponse restEntityRes;

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
    public ResponseEntity<?> getAll(@RequestParam(required = false) String keyword, Pageable pageable) {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(licenseHistoryService.getAll(keyword, pageable)).getResponse();
    }

    @GetMapping("/{id}")
    public LicenseHistory getById(@PathVariable Long id) {
        return licenseHistoryService.getById(id);
    }
}
