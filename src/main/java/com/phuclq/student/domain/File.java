package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class File extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "is_vip")
    private Boolean isVip;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "reading")
    private Integer reading;

    @Column(name = "dowloading")
    private Integer dowloading;

    @Column(name = "view")
    private Integer view;

    @Column(name = "start_page_number")
    private Integer startPageNumber;

    @Column(name = "end_page_number")
    private Integer endPageNumber;

    @Column(name = "total_page_number")
    private Integer totalPageNumber;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "file_group")
    private Integer fileGroup;

    @Column(name = "language_id")
    private Integer languageId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "specialization_id")
    private Integer specializationId;

    @Column(name = "industry_id")
    private Integer industryId;

    @Column(name = "file")
    private String file;

    @Column(name = "file_cut", columnDefinition = "LONGTEXT")
    private String fileCut;

    @Column(name = "file_hashcode", columnDefinition = "LONGTEXT")
    private String fileHashcode;

    @Column(name = "total_comment")
    private Integer totalComment;

    @Column(name = "total_like")
    private Integer totalLike;

    @Column(name = "TOTAL_CARD")
    private Integer totalCard;

    @Column(name = "is_like")
    private Boolean isLike;

    @Column(name = "is_card")
    private Boolean isCard;

    @Column(name = "delete_id")
    private Integer deleteId;

    @Column(name = "delete_date")
    private Timestamp deleteDate;

    @Column(name = "IS_DUPLICATE")
    private Boolean isDuplicate;

    @Column(name = "FILE_DUPLICATE")
    private Integer fileDuplicate;

    @Column(name = "MONEY_TOP")
    private Double moneyTop;

    @Column(name = "START_MONEY_TOP")
    private LocalDateTime startMoneyTop;

    @Column(name = "END_MONEY_TOP")
    private LocalDateTime endMoneyTop;

    @Column(name = "CODE_FILE")
    private String codeFile;

    @Column(name = "ID_URL", columnDefinition = "LONGTEXT")
    private String idUrl;

    public File(Integer loginId) {
        this.dowloading = 0;
        this.view = 0;
        this.reading = 0;
        this.rating = 0.0;
        this.view = 0;
        this.authorId = loginId;
        this.isDeleted = false;
    }

    public File() {
    }


}
