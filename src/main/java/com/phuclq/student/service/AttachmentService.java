package com.phuclq.student.service;

import com.phuclq.student.domain.Attachment;
import com.phuclq.student.dto.AttachmentDTO;
import com.phuclq.student.dto.FileData;
import com.phuclq.student.dto.RequestFileDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface AttachmentService {

    @Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class})
    List<Attachment> createListAttachmentsFromBase64S3(List<RequestFileDTO> files, Integer requestId, Integer loginId, Boolean deleteFile)
            throws IOException;

    @Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class})
    List<Attachment> createListAttachmentsFromBase64S3MultipartFile(List<FileData> files, Integer requestId, Integer loginId, Boolean deleteFile)
            throws IOException;

    void deleteAttachmentByRequestId(Integer id, String fileType);

    AttachmentDTO getAttachmentByIdFromS3Update(Long id, String fileType, HttpServletRequest request) throws IOException;

    AttachmentDTO getAttachmentByRequestIdFromS3(Integer requestId, String fileType)
            throws IOException;

    List<AttachmentDTO> getAttachmentByRequestIdFromS3S(Integer requestId, String fileType)
            throws IOException;

    List<AttachmentDTO> getAttachmentByRequestIdFromS3AndTypes(Integer requestId, List<String> fileType);

    void delete(Long id);

    List<Attachment> getAttachmentByRequestIdAndType(Integer requestId, String type);

}
