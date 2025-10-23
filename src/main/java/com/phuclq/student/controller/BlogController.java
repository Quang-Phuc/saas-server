package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.UserHistoryBlog;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.blog.BlogDetailDto;
import com.phuclq.student.dto.blog.BlogRequest;
import com.phuclq.student.dto.blog.BlogResultDto;
import com.phuclq.student.service.BlogService;
import com.phuclq.student.types.ActivityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private RestEntityResponse restEntityRes;


    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> creatOrUpdate(@RequestBody BlogRequest blog) throws IOException {
        Long jobServiceCV = blogService.create(blog);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchBlog(@RequestBody BlogRequest jobRequest, Pageable pageable) throws IOException {
        jobRequest.setApprove(1);
        BlogResultDto resultDto = blogService.searchBlog(pageable, jobRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(resultDto).getResponse();
    }


    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        blogService.deleteBlogById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/detail/{idUrl}")
    public ResponseEntity<?> findById( @PathVariable String idUrl) {
        BlogDetailDto blogById = blogService.findAllBlogById(null, idUrl);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(blogById).getResponse();
    }


    @PostMapping("/user")
    public ResponseEntity<?> myHome(@RequestBody BlogRequest request, Pageable pageable) {
        BlogResultDto page = blogService.myHome(request, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @GetMapping("/total")
    public ResponseEntity<?> total() {
        TotalMyDTO page = blogService.total();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/like")
    public ResponseEntity<String> likeDocument(@PathVariable("id") Long id) {
        String result;
        HttpStatus status;
        UserHistoryBlog historyFile = blogService.activityHome(id, ActivityConstants.LIKE_BLOG);
        if (historyFile == null) {
            status = HttpStatus.ACCEPTED;
            result = "Không thành công";
        } else {
            status = HttpStatus.OK;
            result = "Thành công";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @DeleteMapping("/{id}/unlike")
    public ResponseEntity<?> unLike(@PathVariable("id") Long id) {
        blogService.deleteActivityHome(id, ActivityConstants.LIKE_BLOG);
        String result = "Bản ghi đã bị loại bỏ khỏi danh sách yêu thích";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFileHistory(@RequestBody BlogRequest request) {
        Page<HistoryFileResult> page = blogService.deleteHistory(request);
        return new ResponseEntity<Page<HistoryFileResult>>(page, HttpStatus.OK);
    }

}
