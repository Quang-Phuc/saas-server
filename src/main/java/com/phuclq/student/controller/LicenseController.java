package com.phuclq.student.controller;


import com.phuclq.student.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/check")
    public ResponseEntity<?> checkLicense() {
        return licenseService.checkLicense();
    }
}
