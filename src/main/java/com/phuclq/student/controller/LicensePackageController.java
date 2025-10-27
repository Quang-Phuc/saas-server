package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.CategoryBLog;
import com.phuclq.student.domain.LicensePackage;
import com.phuclq.student.dto.QRRequest;
import com.phuclq.student.dto.QRResponse;
import com.phuclq.student.service.LicensePackageService;
import com.phuclq.student.service.LicensePayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/license-packages")
@RequiredArgsConstructor
public class LicensePackageController {

    @Autowired
    private RestEntityResponse restEntityRes;

    private final LicensePackageService licensePackageService;

    private final LicensePayService licensePayService;

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

    @PostMapping("/qr")
    public ResponseEntity<QRResponse> getVietQr(@RequestBody QRRequest request)
    {
        try {

            QRResponse base64 = licensePayService.generateQrBase64(request.getAmount(),request.getContent());
            return ResponseEntity.ok(base64);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return null;
        }
    }
}
