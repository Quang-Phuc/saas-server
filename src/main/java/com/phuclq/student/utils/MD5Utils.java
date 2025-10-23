package com.phuclq.student.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String calculateMD5(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        try (InputStream inputStream = file.getInputStream()) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] md5Digest = md.digest();
            return convertByteArrayToHexString(md5Digest);
        }
    }

    private static String convertByteArrayToHexString(byte[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : array) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // Example usage
        MultipartFile file = null; // Your MultipartFile object
        String md5 = calculateMD5(file);
        System.out.println("MD5 hash: " + md5);
    }
}

