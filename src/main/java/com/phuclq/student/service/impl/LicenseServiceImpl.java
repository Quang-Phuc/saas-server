package com.phuclq.student.service.impl;

import com.phuclq.student.domain.LicensePackage;
import com.phuclq.student.domain.User;
import com.phuclq.student.domain.UserLicense;
import com.phuclq.student.dto.LicenseStatusDto;
import com.phuclq.student.dto.StoreInfoDto;
import com.phuclq.student.dto.UserInfoDto;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.repository.LicensePackageRepository;
import com.phuclq.student.repository.StoreRepository;
import com.phuclq.student.repository.UserLicenseRepository;
import com.phuclq.student.repository.UserRepository;
import com.phuclq.student.service.LicenseService;
import com.phuclq.student.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LicenseServiceImpl implements LicenseService {

    private final UserService userService;
    private final UserLicenseRepository userLicenseRepository;
    private final LicensePackageRepository licensePackageRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Override
    public ResponseEntity<?> checkLicense() {
        // ✅ Lấy thông tin user đăng nhập
        UserDTO userResultLogin = userService.getUserResultLogin();
        Integer userId = userResultLogin.getId();
        User user = userRepository.findById(userId).orElse(null);
        Long storeId = user.getStoreId();

        // ✅ Lấy license mới nhất của user
        UserLicense userLicense = userLicenseRepository.findTopByUserIdOrderByExpiryDateDesc(userId)
                .orElse(null);

        if (userLicense == null) {
            // Không có license nào
            return ResponseEntity.ok(new LicenseStatusDto("not_found"));
        }

        // ✅ Kiểm tra license hết hạn
        if (userLicense.isExpired()) {
            List<StoreInfoDto> stores = storeRepository.findAllByOwnerId(userId)
                    .stream()
                    .map(store -> new StoreInfoDto(store.getId(), store.getName(), store.getAddress()))
                    .toList();

            List<UserInfoDto> users = userRepository.findAllByStoreIdAndRoleId(storeId, 3)
                    .stream()
                    .map(u -> new UserInfoDto(u.getId(), u.getFullName(), u.getEmail(), u.getPhone()))
                    .toList();

            LicenseStatusDto expired = new LicenseStatusDto(
                    "expired",
                    stores.size(),
                    users.size(),
                    stores,
                    users
            );
            return ResponseEntity.ok(expired);
        }

        // ✅ Gói còn hạn
        LicensePackage licensePackage = licensePackageRepository.findById(userLicense.getLicensePackageId())
                .orElse(null);

        List<StoreInfoDto> stores = storeRepository.findAllByOwnerId(userId)
                .stream()
                .map(store -> new StoreInfoDto(store.getId(), store.getName(), store.getAddress()))
                .toList();

        List<UserInfoDto> users = userRepository.findAllByStoreIdAndRoleId(storeId, 3)
                .stream()
                .map(u -> new UserInfoDto(u.getId(), u.getFullName(), u.getEmail(), u.getPhone()))
                .toList();

        LicenseStatusDto valid = new LicenseStatusDto("valid", userLicense.getExpiryDate());
        valid.setCurrentStoreCount(stores.size());
        valid.setCurrentUserCount(users.size());
        valid.setStores(stores);
        valid.setUsers(users);

        return ResponseEntity.ok(valid);
    }

}
