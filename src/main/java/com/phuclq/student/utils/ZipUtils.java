package com.phuclq.student.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.FileCopyUtils;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static MultipartFile createZipFile(List<MultipartFile> files, String name) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                byte[] fileBytes = FileCopyUtils.copyToByteArray(file.getInputStream());
                ZipEntry zipEntry = new ZipEntry("file_" + i + "_" + name + "." + getFileExtension(file.getOriginalFilename()));
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(fileBytes);
                zipOutputStream.closeEntry();
            }
        }

        // Convert ByteArrayOutputStream to MultipartFile
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return "files_" + name + ".zip";
            }

            @Override
            public String getContentType() {
                return "application/zip";
            }

            @Override
            public boolean isEmpty() {
                return byteArrayOutputStream.size() == 0;
            }

            @Override
            public long getSize() {
                return byteArrayOutputStream.size();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return byteArrayOutputStream.toByteArray();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                new FileOutputStream(dest).write(byteArrayOutputStream.toByteArray());
            }
        };
    }

    private static String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}

