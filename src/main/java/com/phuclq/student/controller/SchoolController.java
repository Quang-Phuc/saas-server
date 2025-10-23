package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.School;
import com.phuclq.student.domain.SchoolType;
import com.phuclq.student.dto.SchoolResultDto;
import com.phuclq.student.dto.school.SchoolRequest;
import com.phuclq.student.dto.school.SchoolResultDetail;
import com.phuclq.student.service.SchoolService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("")
    public ResponseEntity<?> getSchool(Pageable pageable,@RequestBody SchoolRequest dto) {

        SchoolResultDto result = schoolService.findAll(pageable, dto);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> saveOrUpdate(@RequestBody SchoolRequest school) throws IOException {
        School schoolResult = schoolService.saveOrUpdate(school);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(schoolResult).getResponse();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteSchool(@PathVariable Long Id) {

        schoolService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/detail/{idUrl}")
    public ResponseEntity<?> findById(@PathVariable String idUrl) throws IOException {
        SchoolResultDetail school = schoolService.findAllById(idUrl);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(school).getResponse();
    }

    @PutMapping("/createFromExcel")
    public ResponseEntity<?> saveSchools() {
        List<School> schools = schoolService.saveSchools();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(schools).getResponse();
    }


    @GetMapping("/school-type")
    public ResponseEntity<?> getSchoolType(@RequestParam String type) {
        List<SchoolType> result = schoolService.getSchoolType(type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }
}
