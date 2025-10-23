package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Industry;
import com.phuclq.student.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IndustryController {
    @Autowired
    private IndustryService industryService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/industry")
    public ResponseEntity<?> getAllIndustry(Pageable pageable, String search) {

        Page<Industry> result = industryService.findAll(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @GetMapping("/industry-all")
    public ResponseEntity<?> getAllIndustryAll() {

        List<Industry> result = industryService.findAllIndustry();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/industry/create")
    public ResponseEntity<?> createIndustry(@RequestBody Industry industry) {
        Industry industryResult = industryService.save(industry);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(industryResult).getResponse();
    }

    @PutMapping("/industry/update")
    public ResponseEntity<?> updateIndustry(@RequestBody Industry industry) {

        Industry industryResult = industryService.update(industry);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(industryResult).getResponse();
    }

    @DeleteMapping("/industry/delete/{Id}")
    public ResponseEntity<?> deleteIndustry(@PathVariable int Id) {

        industryService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/industry/{id}")
    public ResponseEntity<?> findByIdIndustry(@PathVariable int id) {
        Industry industry = industryService.findAllById(id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(industry).getResponse();
    }
}
