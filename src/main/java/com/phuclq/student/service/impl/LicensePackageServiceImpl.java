package com.phuclq.student.service.impl;


import com.phuclq.student.domain.LicensePackage;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.LicensePackageRepository;
import com.phuclq.student.service.LicensePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicensePackageServiceImpl implements LicensePackageService {

    private final LicensePackageRepository licensePackageRepository;

    @Override
    public LicensePackage create(LicensePackage licensePackage) {
        // Check trùng tên
        if (licensePackageRepository.findByName(licensePackage.getName()).isPresent()) {
            throw new BusinessHandleException("SS008"); // Gói license đã tồn tại
        }
        return licensePackageRepository.save(licensePackage);
    }

    @Override
    public LicensePackage update(Long id, LicensePackage licensePackage) {
        LicensePackage existing = licensePackageRepository.findById(id)
                .orElseThrow(() -> new BusinessHandleException("SS009")); // Không tìm thấy gói license

        existing.setName(licensePackage.getName());
        existing.setDescription(licensePackage.getDescription());
        existing.setMaxStore(licensePackage.getMaxStore());
        existing.setMaxUserPerStore(licensePackage.getMaxUserPerStore());
        existing.setPrice(licensePackage.getPrice());
        existing.setDiscount(licensePackage.getDiscount());
        existing.setDurationDays(licensePackage.getDurationDays());

        return licensePackageRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        LicensePackage pkg = licensePackageRepository.findById(id)
                .orElseThrow(() -> new BusinessHandleException("SS009"));
        licensePackageRepository.delete(pkg);
    }

    @Override
    public LicensePackage getById(Long id) {
        return licensePackageRepository.findById(id)
                .orElseThrow(() -> new BusinessHandleException("SS009"));
    }

    @Override
    public List<LicensePackage> getAll() {
        return licensePackageRepository.findAll();
    }
}

