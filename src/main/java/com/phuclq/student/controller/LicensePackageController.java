package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.CategoryBLog;
import com.phuclq.student.domain.LicensePackage;
import com.phuclq.student.service.LicensePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/license-packages")
@RequiredArgsConstructor
public class LicensePackageController {

    @Autowired
    private RestEntityResponse restEntityRes;

    private final LicensePackageService licensePackageService;

    @PostMapping
    public LicensePackage create(@RequestBody LicensePackage licensePackage) {
        return licensePackageService.create(licensePackage);
    }

    @PutMapping("/{id}")
    public LicensePackage update(@PathVariable Long id, @RequestBody LicensePackage licensePackage) {
        return licensePackageService.update(id, licensePackage);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        licensePackageService.delete(id);
    }

    @GetMapping("/{id}")
    public LicensePackage getById(@PathVariable Long id) {
        return licensePackageService.getById(id);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(licensePackageService.getAll()).getResponse();
    }

}
