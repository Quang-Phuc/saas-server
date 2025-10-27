package com.phuclq.student.service;



import com.phuclq.student.domain.LicensePackage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LicenseService {

    ResponseEntity<?> checkLicense();
}
