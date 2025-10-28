package com.phuclq.student.service.impl;

import com.phuclq.student.domain.LicenseHistory;
import com.phuclq.student.domain.LicensePackage;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.LicenseHistoryRepository;
import com.phuclq.student.repository.LicensePackageRepository;
import com.phuclq.student.service.LicenseHistoryService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.LicenseHistoryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.phuclq.student.types.LicenseHistoryStatus.PENDING_RENEWAL;

@Service
@RequiredArgsConstructor
public class LicenseHistoryServiceImpl implements LicenseHistoryService {

    private final UserService userService;

    private final LicenseHistoryRepository licenseHistoryRepository;
    private final LicensePackageRepository licensePackageRepository;



    @Override
    public LicenseHistory create(LicenseHistory licenseHistory) {
        UserDTO userResultLogin = userService.getUserResultLogin();

        // 🔹 Lấy thông tin gói license hiện tại
        LicensePackage licensePackage = licensePackageRepository.findById(licenseHistory.getLicensePackageId())
                .orElseThrow(() -> new BusinessHandleException("SS009")); // "Không tìm thấy gói license"

        // 🔹 Tạo lịch sử license
        LicenseHistory history = new LicenseHistory();
        history.setUserId(userResultLogin.getId());
        history.setNote(licenseHistory.getNote());
        history.setStatus(LicenseHistoryStatus.PENDING_RENEWAL.getCode());

        // 🔹 Ghi lại snapshot gói tại thời điểm này
        history.setLicensePackageId(licensePackage.getId());
        history.setPackageName(licensePackage.getName());
        history.setPackagePrice(licensePackage.getPrice());
        history.setPackageDiscount(licensePackage.getDiscount());
        history.setPackageDurationDays(licensePackage.getDurationDays());


        return licenseHistoryRepository.save(history);
    }


    @Override
    public LicenseHistory update(Long id, LicenseHistory licenseHistory) {
        LicenseHistory existing = licenseHistoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch sử license id = " + id));

        existing.setActionDate(licenseHistory.getActionDate());
        existing.setNote(licenseHistory.getNote());

        return licenseHistoryRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!licenseHistoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy lịch sử license id = " + id);
        }
        licenseHistoryRepository.deleteById(id);
    }

    @Override
    public Page<LicenseHistory> getAll(String keyword, Pageable pageable) {
        UserDTO userResultLogin = userService.getUserResultLogin();
        Integer userId = userResultLogin.getId();

        if (keyword == null || keyword.trim().isEmpty()) {
            // Không có từ khóa → lấy tất cả lịch sử theo userId
            return licenseHistoryRepository.findByUserId(userId, pageable);
        }

        // Có từ khóa → tìm theo userId + tên gói
        return licenseHistoryRepository.findByUserIdAndPackageNameContainingIgnoreCase(userId, keyword.trim(), pageable);
    }

    @Override
    public LicenseHistory getById(Long id) {
        return licenseHistoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch sử license id = " + id));
    }
}
