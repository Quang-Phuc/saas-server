package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.blog.BlogRequest;
import com.phuclq.student.dto.blog.BlogResultDto;
import com.phuclq.student.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/blog")
public class AdminBlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private RestEntityResponse restEntityRes;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve")
    public ResponseEntity<?> approve(@RequestBody BlogRequest blogRequest) throws IOException {
        blogService.approve(blogRequest.getId());
        String result = "approve success";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody BlogRequest jobRequest) throws IOException {
        Pageable pageable = PageRequest.of(jobRequest.getPage(), jobRequest.getSize());
        BlogResultDto jobServiceCV = blogService.searchBlog(pageable, jobRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


}
