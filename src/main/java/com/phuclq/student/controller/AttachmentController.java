package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Attachment;
import com.phuclq.student.dto.RequestFileDTO;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    @Autowired
    UserService userService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @DeleteMapping("/{Id}")
    public ResponseEntity<?> likeFile(@PathVariable Long Id) {
        attachmentService.delete(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody List<RequestFileDTO> files) throws IOException {

        List<Attachment> listAttachmentsFromBase64S3 = attachmentService.createListAttachmentsFromBase64S3(files, Integer.MAX_VALUE, userService.getUserLogin().getId(), false);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(listAttachmentsFromBase64S3).getResponse();
    }


}
