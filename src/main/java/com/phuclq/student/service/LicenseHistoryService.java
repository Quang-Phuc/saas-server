package com.phuclq.student.service;

import com.phuclq.student.domain.LicenseHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;

public interface LicenseHistoryService {
    LicenseHistory create(LicenseHistory licenseHistory);
    LicenseHistory update(Long id, LicenseHistory licenseHistory);
    void delete(Long id);
    Page<LicenseHistory> getAll(String keyword, Pageable pageable);
    LicenseHistory getById(Long id);
}
