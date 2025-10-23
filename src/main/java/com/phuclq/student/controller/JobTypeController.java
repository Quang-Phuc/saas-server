package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.JobType;
import com.phuclq.student.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job-type")
public class JobTypeController {

    @Autowired
    private JobTypeService jobTypeService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("")
    public ResponseEntity<?> getAllProvince(Pageable pageable, String search) {

        Page<JobType> result = jobTypeService.findAll(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProvince(@RequestBody JobType jobType) {
        JobType jobType1 = jobTypeService.save(jobType);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobType1).getResponse();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProvince(@RequestBody JobType jobType) {

        JobType jobType1 = jobTypeService.update(jobType);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobType1).getResponse();
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long Id) {

        jobTypeService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/id")
    public ResponseEntity<?> findByIdProvince(@PathVariable Long id) {
        JobType allById = jobTypeService.findAllById(id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(allById).getResponse();
    }


}
