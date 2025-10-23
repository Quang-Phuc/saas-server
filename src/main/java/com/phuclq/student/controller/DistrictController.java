package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.District;
import com.phuclq.student.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/district")
public class DistrictController {
    @Autowired
    private DistrictService districtService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/")
    public ResponseEntity<?> getAllDistrict() {

        List<District> result = districtService.findAll();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @GetMapping("/province")
    public ResponseEntity<?> findAllByDistrict(@RequestParam Integer provinceId) {

        List<District> result = districtService.findAllByProvince(provinceId);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }


}
