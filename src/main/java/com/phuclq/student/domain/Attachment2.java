package com.phuclq.student.domain;

import com.phuclq.student.types.AttachmentStatusType;
import com.phuclq.student.utils.StringUtils;
import lombok.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ATTACHMENT")
@Getter
@Setter
@Builder
@Table(name = "ATTACHMENT2")
public class Attachment2 extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "ATTACHMENT_SEQUENCE", sequenceName = "ATTACHMENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTACHMENT_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "LICENSE_BASE64", columnDefinition = "LONGTEXT")
    private String licenseBase64;

    @Column(name = "FILE_TYPE", nullable = false)
    private String fileType;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "FILE_NAME_S3")
    private String fileNameS3;

    @Column(name = "REQUEST_ID")
    private Integer requestId;

    @Column(name = "URL", columnDefinition = "LONGTEXT")
    private String url;

    @Column(name = "DATA_UIR")
    private String dataUir;

    @Column(name = "CODE_FILE")
    private String codeFile;

    @Column(name = "CHECK_DUPLICATE")
    private Boolean checkDuplicate;

    public Attachment2(MultipartFile file) throws IOException {
        this.licenseBase64 = new String(Base64.decodeBase64(file.getBytes()));
        this.fileType = file.getContentType();
    }

    public Attachment2(String fileName, String fileBase64, String fileType, Integer requestId,
                       String type, String fileNameS3, String url, String extension, String dataUir, int size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.type = type;
        this.requestId = requestId;

        this.fileNameS3 = fileNameS3;
        this.url = url;
        this.status = AttachmentStatusType.ACTIVE.getName();
        this.type = extension;
        this.dataUir = dataUir;
        this.codeFile = fileBase64;
        this.setIdUrl(StringUtils.getSearchableStringUrl(fileName, size));
    }

}
