package com.phuclq.student.service;

import org.springframework.web.multipart.MultipartFile;
// Một class đơn giản để trả về cả URL và S3 Key (nếu cần)
import lombok.AllArgsConstructor;
import lombok.Data;

public interface FileStorageService {
    FileUploadResult saveFile(MultipartFile file);
}