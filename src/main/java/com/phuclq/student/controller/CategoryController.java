package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Category;
import com.phuclq.student.dto.CategoryFileDTO;
import com.phuclq.student.dto.CategoryHomeDTO;
import com.phuclq.student.dto.FileByCategoryDto;
import com.phuclq.student.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory(Pageable pageable, String search) {

        Page<Category> result = categoryService.findAll(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/category/create")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        Category categorySave = categoryService.save(category);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categorySave)
                .getResponse();
    }

    @PutMapping("/category/update")
    public ResponseEntity<?> updateCategory(@RequestBody Category categoryDTO) {

        Category category = categoryService.update(categoryDTO);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(category).getResponse();
    }

    @DeleteMapping("/category/delete/{Id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long Id) {

        categoryService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/category/{Id}")
    public ResponseEntity<?> findByIdCategory(@PathVariable Long Id) {
        Category category = categoryService.findAllById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(category).getResponse();
    }

    @GetMapping("/category/home")
    public ResponseEntity<?> findCategoriesHome() {
        List<CategoryHomeDTO> categoryHomeDTOList = categoryService.getCategoriesHome();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categoryHomeDTOList)
                .getResponse();
    }

    @GetMapping("/category/file")
    public ResponseEntity<?> findFileFromCategories(Pageable pageable) {
        Page<CategoryFileDTO> categories = categoryService.findFileFromCategories(pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categories.getContent())
                .getResponse();
    }

    @GetMapping("/category/file/{id}")
    public ResponseEntity<?> findFileFromCategories(@PathVariable("id") Long id, Pageable pageable) {
        FileByCategoryDto files = categoryService.fileFromCategorie(id, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(files).getResponse();
    }

}
