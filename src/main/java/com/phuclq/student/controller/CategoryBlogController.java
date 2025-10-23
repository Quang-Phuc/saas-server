package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.CategoryBLog;
import com.phuclq.student.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/category/blog")
public class CategoryBlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private RestEntityResponse restEntityRes;


    @PostMapping("/create")
    public ResponseEntity<?> creatOrUpdateJob(@RequestBody CategoryBLog categoryBLog) throws IOException {
        Long categoryBlog = blogService.createCategoryBlog(categoryBLog);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categoryBlog).getResponse();
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProvince(Pageable pageable, String search) {

        Page<CategoryBLog> result = blogService.findAll(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/search")
    public ResponseEntity<?> search() throws IOException {
        List<CategoryBLog> jobServiceCV = blogService.searchCategoryBlog();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        blogService.deleteCategoryBlogById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long Id) {
        CategoryBLog allCategoryBlogById = blogService.findAllCategoryBlogById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(allCategoryBlogById).getResponse();
    }


}
