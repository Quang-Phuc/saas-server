//package com.phuclq.student.controller;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/v1/license")
//@RequiredArgsConstructor
//public class LicenseController {
//
//    private final LicenseService licenseService;
//
//    // 1. Lấy danh sách gói license
//    @GetMapping("/packages")
//    public ResponseEntity<?> getAllPackages() {
//        return ResponseEntity.ok(licenseService.getAllPackages());
//    }
//
//    // 2. Mua hoặc gia hạn license
//    @PostMapping("/buy")
//    public ResponseEntity<?> buyLicense(@RequestParam Long userId,
//                                        @RequestParam Long packageId) {
//        UserLicenseHistory result = licenseService.buyLicense(userId, packageId);
//        return ResponseEntity.ok(result);
//    }
//
//    // 3. Xem lịch sử mua license
//    @GetMapping("/history/{userId}")
//    public ResponseEntity<?> getLicenseHistory(@PathVariable Long userId) {
//        return ResponseEntity.ok(licenseService.getLicenseHistory(userId));
//    }
//}
