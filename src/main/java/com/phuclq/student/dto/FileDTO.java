package com.phuclq.student.dto;

import com.phuclq.student.domain.File;
import lombok.Data;

import java.util.Date;

@Data
public class FileDTO {
    private Integer fileId;
    private String fileTitle;
    private Integer countView;
    private Integer countDownload;
    private String urlImage;
    private String fileLink;
    private String authorName;
    private Date updateDate;
    private Integer totalPageNumber;
    private String description;
    private String fileCut;
    private String categoryName;
    private Long categoryId;
    private String industryName;
    private byte[] fileBase64;
    private Double filePrice;

    public FileDTO(File file) {
        this.fileId = file.getId();
        this.fileTitle = file.getTitle();
        this.countView = file.getView();
        this.countDownload = file.getDowloading();
//        this.urlImage = file.getImage();
        this.updateDate = file.getUpdatedDate();
        this.totalPageNumber = file.getTotalPageNumber();
        this.description = file.getDescription();
//        this.fileCut = file.getFileCut();
    }

    public FileDTO() {
    }
}
