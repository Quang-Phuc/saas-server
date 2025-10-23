package com.phuclq.student.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phuclq.student.domain.File;
import com.phuclq.student.dto.*;
import org.dom4j.DocumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface FileService {

    Page<File> findFilesByCategory(Integer categoryId, Pageable pageable);

    FileDTO getFile(Integer id);

    Page<File> searchFiles(Integer category, Integer specialization, Integer school, String title,
                           Boolean isVip, Float price, Pageable pageable);

    void uploadFile(FileUploadRequest fileUploadRequest)
            throws Exception;

    File uploadFileAdmin(FileUploadRequest fileUploadRequest)
            throws IOException, com.itextpdf.text.DocumentException;

    boolean registryFileVip(Integer userId);

    AttachmentDTO downloadDocument(DownloadFileDTO downloadFileDTO)
            throws DocumentException, com.itextpdf.text.DocumentException, IOException;

    void approveFile(Integer id) throws IOException;

    List<CategoryHomeDTO> getCategoriesHome();

    CategoryHomeFileResult filesPage(FileHomePageRequest request, Pageable pageable);

    List<FileMyMapResult> filesPageMyUser(FileHomePageRequest request);

    FileResultDto searchFileCategory(FileHomePageRequest request, Integer categoryId,
                                     Pageable pageable);

    Page<FileApprove> getFileUnApprove(Pageable pageable);

    List<File> findTop8FileOrderByIdDesc();

    Page<FileResult> searchfileDownloaded(Integer userId, Pageable pageable);


    FileTotalDTO totalFile();

    void uploadFile2(FileUploadRequest fileUploadRequest) throws IOException;

}
