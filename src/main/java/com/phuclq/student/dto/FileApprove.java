package com.phuclq.student.dto;

import com.phuclq.student.domain.File;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FileApprove {
    private Integer id;
    private String title;
    private String file;
    private Integer pageStart;
    private Integer pageEnd;
    private String author;
    private Timestamp createDate;

    public FileApprove(File file) {
        this.id = file.getId();
        this.title = file.getTitle();
//		this.file = file.getFile();
        this.pageStart = file.getStartPageNumber();
        this.pageEnd = file.getEndPageNumber();
//		this.createDate = file.getCreatedDate();
    }

}
