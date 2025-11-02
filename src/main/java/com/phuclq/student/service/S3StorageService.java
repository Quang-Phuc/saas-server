package com.phuclq.student.service;

import com.amazonaws.services.s3.model.Bucket;
import com.phuclq.student.domain.Attachment;
import com.phuclq.student.dto.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface S3StorageService {
    String uploadFileToS3(MultipartFile file) throws IOException;

    Attachment uploadFileToS3(MultipartFile file, Long requestId, String type) throws IOException;

    String uploadFileToS3(FileData file) throws IOException;

    String uploadFileToS3ByBase64(String base64String, String contentType, String fileName)
            throws IOException, NoSuchAlgorithmException;

    String downloadFileFromS3(String fileName) throws IOException;

    MultipartFile  downloadFileFromS3MultipartFile(String fileName) throws IOException;

    List<Bucket> showListBucket();

    String getUrlFile(String fileName);
}
