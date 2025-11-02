package com.phuclq.student.service.impl;

import com.phuclq.student.service.FileStorageService;
import com.phuclq.student.service.FileUploadResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    // TODO: Thay thế bằng logic upload S3 hoặc lưu file thật
    @Override
    public FileUploadResult saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String originalFileName = file.getOriginalFilename();
            String s3Key = "uploads/" + UUID.randomUUID().toString() + "_" + originalFileName;
            String url = "https://your-s3-bucket.com/" + s3Key;

            System.out.println("Uploading file: " + originalFileName + " to " + url);

            // (Thêm logic upload thật ở đây...)

            return new FileUploadResult(url, s3Key);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi upload file: " + e.getMessage());
        }
    }
}