package com.phuclq.student.controller;


import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/license")
@RequiredArgsConstructor
public class LicenseController {
    @Autowired
    private RestEntityResponse restEntityRes;

    private final LicenseService licenseService;

    @GetMapping("/check")
    public ResponseEntity<?> checkLicense() {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(licenseService.checkLicense()).getResponse();
    }
}
