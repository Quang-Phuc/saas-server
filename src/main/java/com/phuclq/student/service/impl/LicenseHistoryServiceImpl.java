package com.phuclq.student.service.impl;

import com.phuclq.student.domain.LicenseHistory;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.repository.LicenseHistoryRepository;
import com.phuclq.student.service.LicenseHistoryService;
import com.phuclq.student.service.UserService;
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

    @Override
    public LicenseHistory create(LicenseHistory licenseHistory) {
        UserDTO userResultLogin = userService.getUserResultLogin();

        LicenseHistory history = new LicenseHistory();
        history.setUserId(userResultLogin.getId());
        history.setLicensePackageId(licenseHistory.getLicensePackageId());
        history.setStatus(PENDING_RENEWAL.getCode());
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
    public Page<Map<String, Object>> getAll(String keyword, Pageable pageable) {
        Page<Object[]> result = licenseHistoryRepository.searchLicenseHistories(keyword, pageable);

        return result.map(record -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", record[0]);
            map.put("userLicenseId", record[1]);
            map.put("action", record[2]);
            map.put("actionDate", record[3]);
            map.put("note", record[4]);
            map.put("createdBy", record[5]);
            map.put("createdDate", record[6]);
            map.put("lastUpdatedBy", record[7]);
            map.put("lastUpdatedDate", record[8]);
            map.put("idUrl", record[9]);
            map.put("packageName", record[10]);
            map.put("packagePrice", record[11]);
            map.put("packageCreatedDate", record[12]);
            return map;
        });
    }

    @Override
    public LicenseHistory getById(Long id) {
        return licenseHistoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch sử license id = " + id));
    }
}
