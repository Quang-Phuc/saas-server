package com.phuclq.student.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResult {
    private String url;
    private String s3Key; // (Hoặc fileNameS3)
}
