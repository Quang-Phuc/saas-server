package com.phuclq.student.service.impl;

//import com.itextpdf.text.DocumentException;

import com.phuclq.student.common.Constants;
import com.phuclq.student.domain.Attachment;
import com.phuclq.student.dto.AttachmentDTO;
import com.phuclq.student.dto.FileData;
import com.phuclq.student.dto.RequestFileDTO;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.repository.AttachmentRepository;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.S3StorageService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.FileType;
import com.phuclq.student.utils.AddWatermarkBase64;
import com.phuclq.student.utils.Base64ToMultipartFile;
import com.phuclq.student.utils.MD5Utils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {


    private final S3StorageService s3StorageService;

    private final AttachmentRepository attachmentRepository;
    private final UserService userService;
    private final AddWatermarkBase64 addWatermarkBase64 ;


    @Override
    @Transactional
    public List<Attachment> createListAttachmentsFromBase64S3(List<RequestFileDTO> files, Integer requestId, Integer loginId, Boolean deleteFile)
            throws IOException {
        List<Attachment> attachments = new ArrayList<>();
        files.forEach(x -> {
            if (deleteFile) {
                deleteAttachmentByRequestId(requestId, x.getType());
            }


            String base64String = Constants.listImagePublic.contains(x.getType()) || x.getType().startsWith("BANNER_") || x.getType().startsWith("FILE_BLOG")||  x.getType().startsWith("CONTENT_") ? addWatermarkBase64.addLogoInMage(x.getContent()) : x.getContent();
            String dataUir = x.getContent().split(Constants.DOT_COMMA_2)[0];

            String folder = Constants.listImagePublic.contains(x.getType()) || x.getType().startsWith("BANNER_") || x.getType().startsWith("FILE_BLOG")||  x.getType().startsWith("CONTENT_") ? "public/" : "File";
            String dateFormat = new SimpleDateFormat("yyyy-MM-ddhhmmss").format(new Date());
            String fileName = com.phuclq.student.utils.StringUtils.getSearchableString(
                    String.format(Constants.STRING_FORMAT_2_VARIABLE_WITH_UNDERLINED, folder, dateFormat, requestId,
                            x.getName())).replace(" ", "_");
            MultipartFile base64ToMultipartFile = new Base64ToMultipartFile(base64String, dataUir,
                    fileName);
            String url = s3StorageService.getUrlFile(fileName);

            try {
                Attachment attachment = new Attachment(fileName, StringUtils.EMPTY, x.getType(),
                        requestId, x.getAttachmentTypeCode(),
                        s3StorageService.uploadFileToS3(base64ToMultipartFile), url, x.getExtension(), dataUir, 0);
                if (x.getType().equals(FileType.FILE_UPLOAD.getName())) {
                    String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(x.getContent());
                    attachment.setCodeFile(md5);
                }
                attachments.add(attachment);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return attachmentRepository.saveAll(attachments);
    }

    @Override
    public List<Attachment> createListAttachmentsFromBase64S3MultipartFile(List<FileData> files, Integer requestId, Integer loginId, Boolean deleteFile) throws IOException {
         List<Attachment> attachments = new ArrayList<>();
        files.forEach(x -> {
            if (deleteFile) {
                deleteAttachmentByRequestId(requestId, x.getType());
            }

//           String dataUir = x.getContent().split(Constants.DOT_COMMA_2)[0];
           String dataUir = "data:"+x.getFile().getContentType()+";base64";

            String folder = Constants.listImagePublic.contains(x.getType()) || x.getType().startsWith("BANNER_") || x.getType().startsWith("FILE_BLOG")||  x.getType().startsWith("CONTENT_") ? "public/" : "File";
            String dateFormat = new SimpleDateFormat("yyyy-MM-ddhhmmss").format(new Date());
            String fileName = com.phuclq.student.utils.StringUtils.getSearchableString(
                    String.format(Constants.STRING_FORMAT_2_VARIABLE_WITH_UNDERLINED, folder, dateFormat, requestId,
                            x.getFile().getOriginalFilename())).replace(" ", "_");
            String url = s3StorageService.getUrlFile(fileName);
            x.setFileName(fileName);

            try {
                Attachment attachment = new Attachment(fileName, StringUtils.EMPTY, x.getType(),
                        requestId, x.getFile().getContentType(),
                        s3StorageService.uploadFileToS3(x), url, x.getType(), dataUir, 0);
                if (x.getType().equals(FileType.FILE_UPLOAD.getName())) {
                    String md5 = MD5Utils.calculateMD5(x.getFile());
                    attachment.setCodeFile(md5);
                }
                attachments.add(attachment);

            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });

        return attachmentRepository.saveAll(attachments);


    }

    public List<Attachment> getListAttachmentByRequestId(Integer id, String fileType) {
        return this.attachmentRepository.findAllByRequestIdAndFileType(id, fileType);
    }

    @Override
    public void deleteAttachmentByRequestId(Integer id, String fileType) {
        this.attachmentRepository.deleteAll(getListAttachmentByRequestId(id, fileType));
    }

    @Override
    public AttachmentDTO getAttachmentByIdFromS3Update(Long id, String fileType, HttpServletRequest request)
            throws IOException {
        Attachment attachment = getByIdAndType(id, fileType);
        if (!userService.getUserLogin().getId().toString().equals(attachment.getCreatedBy())) {
            throw new BusinessHandleException("SS007");
        }
        return getAttachmentByIdFromS3(attachment);
    }

    public AttachmentDTO getAttachmentByIdFromS3(Attachment attachment) throws IOException {
        String base64FromS3 = s3StorageService.downloadFileFromS3(attachment.getFileNameS3());
        AttachmentDTO attachmentDTO = new AttachmentDTO(attachment);
        attachmentDTO.setMainDocument(attachment.getDataUir() + Constants.DOT_COMMA_2 + base64FromS3);
        return attachmentDTO;
    }

    public Attachment getById(Long id, HttpServletRequest request) {
        Optional<Attachment> attachmentOptional = attachmentRepository.findById(id);
        if (Objects.isNull(attachmentOptional)) {
            throw new IllegalArgumentException((ExceptionUtils.ATTACHMENT_NOT_EXIST));
        }
        return attachmentOptional.get();
    }

    public Attachment getByIdAndType(Long id, String fileType) {
        Optional<Attachment> attachmentOptional = attachmentRepository.findAllByIdAndFileType(id, fileType);
        if (Objects.isNull(attachmentOptional)) {
            throw new IllegalArgumentException((ExceptionUtils.ATTACHMENT_NOT_EXIST));
        }
        return attachmentOptional.get();
    }


    @Override
    public AttachmentDTO getAttachmentByRequestIdFromS3(Integer requestId, String fileType)
            throws IOException {

        List<Attachment> attachmentOptional = attachmentRepository.findAllByRequestIdAndFileType(
                requestId, fileType);
        if (Objects.isNull(attachmentOptional) || attachmentOptional.size() == 0) {
            return null;
        }
        Attachment attachment = attachmentOptional.get(0);
        String base64FromS3 = s3StorageService.downloadFileFromS3(attachment.getFileNameS3());
        AttachmentDTO attachmentDTO = new AttachmentDTO(attachment);
        attachmentDTO.setMainDocument(attachment.getDataUir() + Constants.DOT_COMMA_2 + base64FromS3);
        return attachmentDTO;
    }

    @Override
    public List<AttachmentDTO> getAttachmentByRequestIdFromS3S(Integer requestId, String fileType) throws IOException {
        List<Attachment> attachmentOptional = attachmentRepository.findAllByRequestIdAndFileType(requestId, fileType);
        if (Objects.isNull(attachmentOptional) || attachmentOptional.size() == 0) {
            return null;
        }

        List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
        attachmentOptional.forEach(x -> {
            String base64FromS3 = null;
            try {
                base64FromS3 = s3StorageService.downloadFileFromS3(x.getFileNameS3());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AttachmentDTO attachmentDTO = new AttachmentDTO(x);
            attachmentDTO.setMainDocument(x.getDataUir() + Constants.DOT_COMMA_2 + base64FromS3);
            attachmentDTOList.add(attachmentDTO);
        });
        return attachmentDTOList;


    }

    @Override
    public List<AttachmentDTO> getAttachmentByRequestIdFromS3AndTypes(Integer requestId,
                                                                      List<String> fileType) {

        List<Attachment> attachmentOptional = attachmentRepository.findAllByRequestIdAndFileTypeIn(
                requestId, fileType);

        List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
        attachmentOptional.forEach(attachment -> {
            String base64FromS3 = null;
            try {
                base64FromS3 = s3StorageService.downloadFileFromS3(attachment.getFileNameS3());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AttachmentDTO attachmentDTO = new AttachmentDTO(attachment);
            attachmentDTO.setMainDocument(attachment.getDataUir() + Constants.DOT_COMMA_2 + base64FromS3);
            attachmentDTOList.add(attachmentDTO);
        });

        return attachmentDTOList;
    }

    @Override
    public void delete(Long id) {
        attachmentRepository.deleteById(id);

    }

    @Override
    public List<Attachment> getAttachmentByRequestIdAndType(Integer requestId, String type) {
        return attachmentRepository.findAllByRequestIdAndFileType(requestId, type);
    }
}
