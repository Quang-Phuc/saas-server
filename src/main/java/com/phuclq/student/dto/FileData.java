package com.phuclq.student.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileData {
    private MultipartFile file;
    private String type;
    private String fileName;
}
