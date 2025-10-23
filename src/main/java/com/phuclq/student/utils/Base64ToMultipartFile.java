package com.phuclq.student.utils;

import com.phuclq.student.common.Constants;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64ToMultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private final String extension;
    private final String contentType;
    private final String customName;

    /**
     * @param base64
     * @param dataUri The format is similar to: data:image/png;base64
     */
//  public Base64ToMultipartFile(String base64, String dataUri) {
//    this.fileContent = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
//    this.extension = dataUri.split(Constants.DOT_COMMA)[0].split(Constants.SLASHES)[1];
//    this.contentType = dataUri.split(Constants.DOT_COMMA)[0].split(Constants.DOT_DOT)[1];
//    this.customName = null;
//  }
    public Base64ToMultipartFile(String base64, String dataUri, String customName) {
        this.fileContent = Base64.getDecoder().decode(base64.split(Constants.DOT_COMMA_2)[1].getBytes(StandardCharsets.UTF_8));
        this.extension = dataUri.split(Constants.DOT_COMMA)[0].split(Constants.SLASHES)[1];
        this.contentType = dataUri.split(Constants.DOT_COMMA)[0].split(Constants.DOT_DOT)[1];
        this.customName = customName;
    }

    public Base64ToMultipartFile(
            String base64, String extension, String contentType, String customName) {
        this.fileContent = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        this.extension = extension;
        this.contentType = contentType;
        this.customName = customName;
    }

    @Override
    public String getName() {
        return customName;
    }

    @Override
    public String getOriginalFilename() {
        return customName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileContent);
        }
    }
}
