package com.phuclq.student.service;


import com.phuclq.student.domain.LicensePackage;
import java.util.List;

public interface LicensePackageService {

    LicensePackage create(LicensePackage licensePackage);

    LicensePackage update(Long id, LicensePackage licensePackage);

    void delete(Long id);

    LicensePackage getById(Long id);

    List<LicensePackage> getAll();
}
