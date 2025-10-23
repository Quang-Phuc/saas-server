package com.phuclq.student.dto;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.domain.Comment;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class FileResult {
    List<Attachment> attachmentOptional;
    AttachmentDTO attachmentDTO;
    List<CommentDTO> commentDTO;
    List<Comment> comments;
    private Integer id;
    private String title;
    private Integer view;
    private Integer download;
    private Double price;
    private String image;
    private String fileView;
    private String createDate;
    private String userName;
    private String urlAuthor;
    private String fileHashCode;
    private Integer totalComment;
    private Integer totalLike;
    private Boolean isLike;
    private Boolean isCard;
    private Boolean isMy;
    private String category;
    private Boolean isVip;
    private Boolean isFileCut;
    private BigInteger categoryId;
    private Integer schoolId;
    private Integer industryId;
    private String description;
    private Integer approver;
    private Integer startPageNumber;
    private Integer endPageNumber;
    private Integer languageId;
    private Integer specializationId;
    private Integer fileDuplicate;
    private Integer approverId;
    private Double totalRate;
    private Double totalRateUser;
    private String createdBy;
    private String idUrl;
    private String idUrlCategory;

    public FileResult(Object[] obj) {
        this.id = (Integer) obj[0];
        this.title = (String) obj[1];
        this.view = (Integer) obj[2];
        this.download = (Integer) obj[3];
        this.price = (Double) obj[4];
        this.image = (String) obj[5];
        this.createDate = (String) obj[6];
        this.totalComment = (Integer) obj[7];
        this.category = (String) obj[8];
        this.totalLike = (Integer) obj[9];
        this.isLike = (Boolean) obj[10];
        this.isCard = (Boolean) obj[11];
        this.isVip = (Boolean) obj[12];
        this.categoryId = (BigInteger) obj[13];
        this.userName = (String) obj[14];
        this.urlAuthor = (String) obj[15];
        this.schoolId = (Integer) obj[16];
        this.industryId = (Integer) obj[17];
        this.description = (String) obj[18];
        this.startPageNumber = (Integer) obj[19];
        this.endPageNumber = (Integer) obj[20];
        this.languageId = (Integer) obj[21];
        this.specializationId = (Integer) obj[22];
        this.fileDuplicate = (Integer) obj[23];
        this.approverId = (Integer) obj[24];
        this.createdBy = (String) obj[25];
        this.idUrl = (String) obj[26];
        this.idUrlCategory = (String) obj[27];
    }

    public FileResult(Object[] obj, Boolean typeFile) {
        this.id = (Integer) obj[0];
        this.approver = (Integer) obj[1];
    }


    public FileResult() {

    }

}
