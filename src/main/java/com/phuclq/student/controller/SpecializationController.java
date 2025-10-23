package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Specialization;
import com.phuclq.student.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SpecializationController {
    @Autowired
    private SpecializationService specializationService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/specialization")
    public ResponseEntity<?> getAllSpecialization(Pageable pageable, String search) {

        Page<Specialization> result = specializationService.findAll(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/specialization/create")
    public ResponseEntity<?> createSpecialization(@RequestBody Specialization specialization) {
        Specialization specializationResult = specializationService.save(specialization);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(specializationResult).getResponse();
    }

    @PutMapping("/specialization/update")
    public ResponseEntity<?> updateSpecialization(@RequestBody Specialization specialization) {

        Specialization specializationResult = specializationService.update(specialization);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(specializationResult).getResponse();
    }

    @DeleteMapping("/specialization/delete/{Id}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable int Id) {

        specializationService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/specialization/id")
    public ResponseEntity<?> findByIdSpecialization(@PathVariable int Id) {
        Optional<Specialization> specialization = specializationService.findAllById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(specialization).getResponse();
    }

    @GetMapping("/specialization/industry/{Id}")
    public ResponseEntity<?> findByIdIndustry(@PathVariable int Id) {
        List<Specialization> specializations = specializationService.findAllIndistry(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(specializations).getResponse();
    }
}
