package com.phuclq.student.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "ATTACHMENTS")
@Data
@NoArgsConstructor
public class Attachment extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;


    @Column(name = "URL", columnDefinition = "LONGTEXT")
    private String url;

    @Column(name = "FILE_TYPE", nullable = false)
    private String fileType;

    private long fileSize;

    @Column(name = "contract_id")
    private String contractId;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "FILE_NAME_S3")
    private String fileNameS3;

    @Column(name = "TYPE")
    private String type;
}