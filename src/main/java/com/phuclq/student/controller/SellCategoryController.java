package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.SellCategory;
import com.phuclq.student.service.SellCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sell-category")
public class SellCategoryController {

    @Autowired
    private SellCategoryService jobTypeService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("")
    public ResponseEntity<?> getAllProvince(Pageable pageable, String search) {

        Page<SellCategory> result = jobTypeService.findAll(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProvince(@RequestBody SellCategory jobType) {
        SellCategory jobType1 = jobTypeService.save(jobType);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobType1).getResponse();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProvince(@RequestBody SellCategory jobType) {

        SellCategory jobType1 = jobTypeService.update(jobType);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobType1).getResponse();
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long Id) {

        jobTypeService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/id")
    public ResponseEntity<?> findByIdProvince(@PathVariable Long id) {
        SellCategory allById = jobTypeService.findAllById(id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(allById).getResponse();
    }


}
