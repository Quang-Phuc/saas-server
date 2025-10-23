package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.File;
import com.phuclq.student.domain.User;
import com.phuclq.student.domain.UserHistory;
import com.phuclq.student.dto.*;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.FileService;
import com.phuclq.student.service.UserHistoryService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.ActivityConstants;
import com.phuclq.student.utils.PaginationUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserHistoryService userHistoryService;

    @Autowired
    private UserService userService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/file")
    public ResponseEntity<List<File>> getFilesByCategory(@PathParam("categoryId") Integer categoryId, Pageable pageable) {
        Page<File> page = fileService.findFilesByCategory(categoryId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<FileDTO> getFile(@PathVariable("id") Integer id) throws Exception {
        if (id == null) {
            throw new Exception("id must not null!");
        }
        FileDTO fileDTO = fileService.getFile(id);
        return ResponseEntity.ok().body(fileDTO);
    }

    @PostMapping("/file/search")
    public ResponseEntity<List<File>> searchFile(@RequestBody FileSearchRequest fileSearchRequest, Pageable pageable) {
        Page<File> page = fileService.searchFiles(fileSearchRequest.getCategory(), fileSearchRequest.getSpecialization(), fileSearchRequest.getSchool(), fileSearchRequest.getTitle(), fileSearchRequest.getIsVip(), fileSearchRequest.getPrice(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestBody FileUploadRequest fileUploadRequest) throws Exception {
        fileService.uploadFile(fileUploadRequest);
        String result = "Đăng file thành công ";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/file/upload2")
    public ResponseEntity<String> uploadFile2(@ModelAttribute FileUploadRequest requestBody) throws Exception {

        fileService.uploadFile2(requestBody);
        String result = "Đăng file thành công ";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }


    @PostMapping("/file/download")
    public ResponseEntity<String> downloadDocument(@RequestBody DownloadFileDTO downloadFileDTO) throws DocumentException, com.itextpdf.text.DocumentException, IOException {

        AttachmentDTO file = fileService.downloadDocument(downloadFileDTO);

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(file).getResponse();
    }

    @GetMapping("/file/{id}/report")
    public ResponseEntity<String> reportDocument(@PathVariable("id") Integer idUrl) throws Exception {
        User user = userService.getUserLogin();
        UserHistory userHistory = userHistoryService.activateFileHistory(user.getId(), idUrl, ActivityConstants.REPORT);
        String result = "";
        HttpStatus status;
        if (userHistory == null) {
            status = HttpStatus.ACCEPTED;
            result = "Tài liệu này đã được báo cáo";
        } else {
            status = HttpStatus.OK;
            result = "Báo cáo vi phạm thành công";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/file/{id}/like")
    public ResponseEntity<String> likeDocument(@PathVariable("id") Integer idUrl) {
        String result;
        HttpStatus status;
        User user = userService.getUserLogin();
        UserHistory historyFile = userHistoryService.activateFileHistory(user.getId(), idUrl, ActivityConstants.LIKE);
        if (historyFile == null) {
            status = HttpStatus.ACCEPTED;
            result = "Tài liệu đã được yêu thích";
        } else {
            status = HttpStatus.OK;
            result = "Tài liệu đã được đưa vào danh sách yêu thích";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @DeleteMapping("/file/{id}/unlike")
    public ResponseEntity<?> unLikeDocument(@PathVariable("id") Integer  id) {
        User user = userService.getUserLogin();
        userHistoryService.deleteActivityByUser(user.getId(), id, ActivityConstants.LIKE);
        String result = "Tài liệu đã được loại bỏ khỏi danh sách yêu thích";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/file/{id}/card")
    public ResponseEntity<String> cardDocument(@PathVariable("id") Integer  id) {
        String result;
        HttpStatus status;
        User user = userService.getUserLogin();
        UserHistory historyFile = userHistoryService.activateFileHistory(user.getId(), id, ActivityConstants.CARD);
        if (historyFile == null) {
            status = HttpStatus.ACCEPTED;
            result = "Tài liệu đã được thêm vào giỏ hàng ";
        } else {
            status = HttpStatus.OK;
            result = "Tài liệu đã được thêm vào giỏ hàn";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/file/{id}/uncard")
    public ResponseEntity<?> unCardDocument(@PathVariable("id") Integer id) {
        User user = userService.getUserLogin();
        userHistoryService.deleteActivityByUser(user.getId(), id, ActivityConstants.CARD);
        String result = "Tài liệu đã được xóa vào giỏ hàng ";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/file/category/suggest")
    public ResponseEntity<List<File>> getSuggestByCategory(@PathParam("categoryId") Integer categoryId) {
        List<String> strings = Arrays.asList("Tài liệu học tập", "Bài tập cuối kỳ");
        HttpStatus status = HttpStatus.OK;
        return restEntityRes.setHttpStatus(status).setDataResponse(strings).getResponse();
    }

    @GetMapping("/file/category/home")
    public ResponseEntity<?> findCategoriesHome() {
        List<CategoryHomeDTO> categoryHomeDTOList = fileService.getCategoriesHome();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categoryHomeDTOList).getResponse();
    }

    @PostMapping("/file/page-home")
    public ResponseEntity<?> fileHomePage(@RequestBody FileHomePageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        CategoryHomeFileResult result = fileService.filesPage(request, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/file/category/search")
    public ResponseEntity<?> searchFileByCategory(@RequestBody FileHomePageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        FileResultDto result = fileService.searchFileCategory(request, request.getCategoryId(), pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @GetMapping("/file/approve")
    public ResponseEntity<?> getFileUnApprove(Pageable pageable) {
        Page<FileApprove> page = fileService.getFileUnApprove(pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @GetMapping("/file/top8")
    public ResponseEntity<?> findTop10OrderByIdDesc() {
        List<File> files = fileService.findTop8FileOrderByIdDesc();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(files).getResponse();
    }

    @GetMapping("/file/downloaded")
    public ResponseEntity<?> getFileDownloaded(Pageable pageable) {
        User user = userService.getUserLogin();
        Page<FileResult> result = fileService.searchfileDownloaded(user.getId(), pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @GetMapping("/file/downloadS3-upload")
    public ResponseEntity<List<File>> downloadS3(@RequestParam Long id, @RequestParam String fileType, HttpServletRequest request) throws IOException {
        AttachmentDTO attachmentByIdFromS3 = attachmentService.getAttachmentByIdFromS3Update(id, fileType, request);

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(attachmentByIdFromS3).getResponse();
    }


    @PostMapping("/file/my-local")
    public ResponseEntity<?> myLocalFile(@RequestBody FileHomePageRequest request) {
        List<FileMyMapResult> result = fileService.filesPageMyUser(request);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }


}
