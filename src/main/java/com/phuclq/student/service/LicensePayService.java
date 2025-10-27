package com.phuclq.student.service;


import com.phuclq.student.dto.QRResponse;

public interface LicensePayService {
    /**
     * Trả QR code Base64 theo licensePackage
     *
     * @param price id package, ví dụ 1, 2, 3
     * @return Base64 string
     */
    QRResponse generateQrBase64(int  price, String content) throws Exception;
}

