package com.phuclq.student.controller.admin;

import com.itextpdf.text.DocumentException;
import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.File;
import com.phuclq.student.domain.PaymentRequest;
import com.phuclq.student.dto.*;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.FileService;
import com.phuclq.student.service.PaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/payment")
public class AdminPaymentController {

    @Autowired
    private FileService fileService;


    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private RestEntityResponse restEntityRes;

    @Autowired
    private PaymentRequestService paymentRequestService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> search(@RequestBody FileHomePageRequest request) {

        PaymentResultDto result = paymentRequestService.searchPayment(request, true);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody PaymentRequestDto paymentRequestDto)
            throws IOException {
        PaymentRequest categorySave = paymentRequestService.save(paymentRequestDto, true);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categorySave)
                .getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable int Id) {

        paymentRequestService.deleteByIdAdmin(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/file/search")
    public ResponseEntity<?> searchFileByCategory(@RequestBody FileHomePageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        FileResultDto result = fileService.searchFileCategory(request, request.getCategoryId(),
                pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/file/downloadS3-request")
    public ResponseEntity<List<File>> downloadS3Request(@RequestParam Integer id, @RequestParam List<String> fileType)
            throws IOException {
        List<AttachmentDTO> attachmentByIdFromS3 = attachmentService.getAttachmentByRequestIdFromS3AndTypes(
                id, fileType);

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(attachmentByIdFromS3)
                .getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/file/upload")
    public ResponseEntity<List<File>> upload(@RequestBody FileUploadRequest FileUploadRequest)
            throws IOException, DocumentException {
        File file = fileService.uploadFileAdmin(FileUploadRequest);

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(file).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/file/total")
    public ResponseEntity<List<File>> total() {
        FileTotalDTO fileTotalDTO = fileService.totalFile();

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(fileTotalDTO).getResponse();
    }

}
