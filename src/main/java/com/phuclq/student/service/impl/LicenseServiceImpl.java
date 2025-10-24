//package com.phuclq.student.service.impl;
//
//
//import com.phuclq.student.repository.UserRepository;
//import com.phuclq.student.service.LicenseService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class LicenseServiceImpl implements LicenseService {
//
//    private final LicensePackageRepository licensePackageRepository;
//    private final UserRepository userRepository;
//    private final UserLicenseRepository userLicenseRepository;
//    private final UserLicenseHistoryRepository userLicenseHistoryRepository;
//
//    @Override
//    public List<LicensePackage> getAllPackages() {
//        return licensePackageRepository.findAll();
//    }
//
//    @Override
//    public UserLicenseHistory buyLicense(Long userId, Long packageId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        LicensePackage licensePackage = licensePackageRepository.findById(packageId)
//                .orElseThrow(() -> new RuntimeException("License package not found"));
//
//        // Tính giá sau giảm
//        double discount = licensePackage.getDiscount() != null ? licensePackage.getDiscount() : 0.0;
//        double price = licensePackage.getPrice();
//        double finalAmount = price - (price * discount / 100);
//
//        // Tạo hoặc cập nhật UserLicense
//        UserLicense userLicense = new UserLicense();
//        userLicense.setUser(user);
//        userLicense.setLicensePackage(licensePackage);
//        userLicense.setStartDate(LocalDateTime.now());
//        userLicense.setEndDate(LocalDateTime.now().plusDays(licensePackage.getDurationDays()));
//        userLicenseRepository.save(userLicense);
//
//        // Lưu lịch sử
//        UserLicenseHistory history = new UserLicenseHistory();
//        history.setUser(user);
//        history.setLicensePackage(licensePackage);
//        history.setPriceAtPurchase(price);
//        history.setDiscountApplied(discount);
//        history.setFinalAmount(finalAmount);
//        history.setStartDate(userLicense.getStartDate());
//        history.setEndDate(userLicense.getEndDate());
//        history.setActionType(UserLicenseHistory.ActionType.MUA_MOI);
//        userLicenseHistoryRepository.save(history);
//
//        return history;
//    }
//
//    @Override
//    public List<UserLicenseHistory> getLicenseHistory(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return userLicenseHistoryRepository.findByUser(user);
//    }
//}
