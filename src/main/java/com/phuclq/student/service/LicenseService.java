package com.phuclq.student.service;



import com.phuclq.student.domain.LicensePackage;
import com.phuclq.student.dto.LicenseStatusDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LicenseService {

    LicenseStatusDto checkLicense();
}
