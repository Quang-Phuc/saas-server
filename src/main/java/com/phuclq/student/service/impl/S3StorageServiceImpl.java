package com.phuclq.student.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.phuclq.student.common.Constants;
import com.phuclq.student.dto.FileData;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class S3StorageServiceImpl implements S3StorageService {

    private final AmazonS3 s3Client;

    @Value("${aws-s3.bucket-name}")
    private String bucketName;

    public S3StorageServiceImpl(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * tải file lên amazon s3
     *
     * @param file file gửi lên
     * @return tên file đồng thời là id để tải file đó
     * @author datlp
     * @since 12/7/2022
     */
    @Override
    public String uploadFileToS3(MultipartFile file) throws IOException {
        if (ObjectUtils.isNotEmpty(file)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            String fileName = file.getOriginalFilename();
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return fileName;
        } else if (!StringUtils.containsIgnoreCase(file.getContentType(), Constants.PDF)) {
            throw new BusinessException(ExceptionUtils.NOT_PDF);
        } else if (file.isEmpty()) {
            throw new BusinessException(ExceptionUtils.FILE_IS_EMPTY);
        }
        return null;
    }

    /**
     * tải file lên amazon s3
     *
     * @param file file gửi lên
     * @return tên file đồng thời là id để tải file đó
     * @author datlp
     * @since 12/7/2022
     */
    @Override
    public String uploadFileToS3(FileData dto) throws IOException {
        MultipartFile file = dto.getFile();

        if (ObjectUtils.isNotEmpty(file)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            String fileName = dto.getFileName();
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return fileName;
        } else if (!StringUtils.containsIgnoreCase(file.getContentType(), Constants.PDF)) {
            throw new BusinessException(ExceptionUtils.NOT_PDF);
        } else if (file.isEmpty()) {
            throw new BusinessException(ExceptionUtils.FILE_IS_EMPTY);
        }
        return null;
    }

    @Override
    // upload without create file but result mime type is plan/text
    public String uploadFileToS3ByBase64(String base64String, String contentType, String fileName)
            throws IOException, NoSuchAlgorithmException {
        if (StringUtils.isNotEmpty(base64String)
                && StringUtils.containsIgnoreCase(contentType, Constants.PDF)) {

            byte[] contentBytes =
                    String.format("data:%s;base64,%S", contentType, base64String)
                            .getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);

            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentLength(contentBytes.length);
            metaData.setContentType(contentType);

            // // try setContentMD5
            //      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //      messageDigest.reset();
            //      messageDigest.update(
            //          IOUtils.toByteArray(
            //              new ByteArrayInputStream(base64String.getBytes(StandardCharsets.UTF_8))));
            //  // content is a passed in InputStream

            //      byte[] resultByte = DigestUtils.md5(messageDigest.digest());
            //      byte[] resultByte = DigestUtils.md5(new
            // ByteArrayInputStream(base64String.getBytes(StandardCharsets.UTF_8)));
            //      byte[] bytes = Base64.encodeBase64(resultByte);
            //      String streamMD5 = new String(Base64.encodeBase64(bytes));
            //      metaData.setContentMD5(streamMD5);

            //      metaData.setContentEncoding(String.valueOf(com.amazonaws.util.StringUtils.UTF8));
            //            metaData.setHeader("Content-Type", contentType);

            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, fileName, inputStream, metaData);
            putObjectRequest.withMetadata(metaData);

            s3Client.putObject(putObjectRequest);
            return fileName;
        } else if (!StringUtils.containsIgnoreCase(contentType, Constants.PDF)) {
            throw new BusinessException(ExceptionUtils.NOT_PDF);
        }
        return null;
    }

    /**
     * tải file xuống từ amazon s3
     *
     * @param fileName tên file cần tải
     * @return tên file đồng thời là id để tải file đó
     * @author datlp
     * @since 12/7/2022
     */
    @Override
    public String downloadFileFromS3(String fileName) {
        S3Object object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
        try {
            byte[] fileContentInByte = IOUtils.toByteArray(s3ObjectInputStream);
            return Base64.getEncoder().encodeToString(fileContentInByte);
        } catch (IOException e) {
            log.error("[LOG downloadFileFromS3] :: Error while downloading " + fileName, e);
        } catch (AmazonS3Exception amazonS3Exception) {
            log.error(
                    "[LOG downloadFileFromS3] :: Error while downloading" + fileName, amazonS3Exception);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public MultipartFile downloadFileFromS3MultipartFile(String fileName) {
        S3Object object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
        try {
            byte[] fileContentInByte = StreamUtils.copyToByteArray(s3ObjectInputStream);
            String originalFilename = extractFileName(fileName);
            String contentType = object.getObjectMetadata().getContentType();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContentInByte);
            return new CustomMultipartFile(originalFilename, contentType, inputStream);
        } catch (IOException e) {
            log.error("[LOG downloadFileFromS3] :: Error while downloading " + fileName, e);
        } catch (AmazonS3Exception amazonS3Exception) {
            log.error("[LOG downloadFileFromS3] :: Error while downloading" + fileName, amazonS3Exception);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (s3ObjectInputStream != null) {
                    s3ObjectInputStream.close();
                }
            } catch (IOException e) {
                log.error("Error closing S3ObjectInputStream", e);
            }
        }
        return null;
    }

    private String extractFileName(String filePath) {
        int separatorIndex = filePath.lastIndexOf("/");
        if (separatorIndex < 0) {
            separatorIndex = filePath.lastIndexOf("\\"); // For Windows file paths
        }
        if (separatorIndex < 0) {
            return filePath; // No separator found, return full path
        }
        return filePath.substring(separatorIndex + 1);
    }

    class CustomMultipartFile implements MultipartFile {

        private final String name;
        private final String contentType;
        private final byte[] content;

        public CustomMultipartFile(String name, String contentType, InputStream inputStream) throws IOException {
            this.name = name;
            this.contentType = contentType;
            this.content = StreamUtils.copyToByteArray(inputStream);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            // Not implemented for this example
        }
    }
    @Override
    public List<Bucket> showListBucket() {
        return s3Client.listBuckets();
    }

    @Override
    public String getUrlFile(String fileName) {
//        return ((AmazonS3Client) s3Client).getResourceUrl(bucketName, fileName);
        return ((AmazonS3Client) s3Client).getResourceUrl(bucketName, fileName);
    }
}
