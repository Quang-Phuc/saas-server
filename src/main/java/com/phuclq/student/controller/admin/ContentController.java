package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Content;
import com.phuclq.student.dto.content.ContentRequest;
import com.phuclq.student.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private RestEntityResponse restEntityRes;


    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("")
    public ResponseEntity<?> creatOrUpdateJob(@RequestBody ContentRequest contentRequest) throws IOException {
        Long jobServiceCV = contentService.creatOrUpdateJob(contentRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("/search")
    public ResponseEntity<?> search(Pageable pageable, String search) throws IOException {
        Page<Content> jobServiceCV = contentService.search(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        contentService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Content industry = contentService.findAllById(id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(industry).getResponse();
    }

    @GetMapping("/detail/type/{type}")
    public ResponseEntity<?> findById(@PathVariable String type) {
        Content industry = contentService.findAllByType(type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(industry).getResponse();
    }


}
