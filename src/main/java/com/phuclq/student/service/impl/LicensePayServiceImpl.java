package com.phuclq.student.service.impl;


import com.phuclq.student.dto.QRResponse;
import com.phuclq.student.properties.VietQrProperties;
import com.phuclq.student.service.LicensePayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class LicensePayServiceImpl implements LicensePayService {

    private final VietQrProperties properties;


    @Override
    public QRResponse generateQrBase64(int price, String content) throws Exception {


        // Tạo URL QR
        String bankId = properties.getBankId();
        String accountNo = properties.getAccountNo();
        String template = properties.getTemplate();
        String accountName = URLEncoder.encode(properties.getAccountName(), StandardCharsets.UTF_8.toString());
        String addInfo = URLEncoder.encode(content, StandardCharsets.UTF_8.toString());

        String urlStr = String.format(
                "https://img.vietqr.io/image/%s-%s-%s.png?amount=%d&addInfo=%s&accountName=%s",
                bankId,
                accountNo,
                template,
                price,
                addInfo,
                accountName
        );

        System.out.println("Đang tải QR từ URL: " + urlStr);

        // Tải ảnh với HttpURLConnection để tránh lỗi SSL/redirect
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.connect();

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IllegalStateException("Không tải được ảnh từ URL (HTTP " + status + "): " + urlStr);
        }

        BufferedImage img;
        try (InputStream in = conn.getInputStream()) {
            img = ImageIO.read(in);
        }

        conn.disconnect();

        if (img == null) {
            throw new IllegalStateException("Không đọc được định dạng ảnh từ URL: " + urlStr);
        }

        // Chuyển ảnh sang base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        QRResponse qrResponse = new QRResponse();
        qrResponse.setBase64Data(Base64.getEncoder().encodeToString(imageBytes));
        return qrResponse;
    }

}

