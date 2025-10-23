package com.phuclq.student.dto;

import com.phuclq.student.domain.Attachment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private Long id;
    private String mainDocument;
    private String description;
    private String attachmentName;
    private String type;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public AttachmentDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.mainDocument = attachment.getLicenseBase64();
        this.description = attachment.getFileType();
        this.attachmentName = attachment.getFileName();
        this.type = attachment.getType();
        this.createdDate = attachment.getCreatedDate();
        this.updatedDate = attachment.getLastUpdatedDate();
    }
}
