package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Ward;
import com.phuclq.student.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ward")
public class WardController {

    @Autowired
    private WardService wardService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/")
    public ResponseEntity<?> getAllDistrict() {

        List<Ward> result = wardService.findAll();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @GetMapping("/district")
    public ResponseEntity<?> findAllByDistrictAndProvince(@RequestParam Integer provinceId,
                                                          @RequestParam Integer districtId) {

        List<Ward> result = wardService.findAllByProvinceAndDistrict(provinceId, districtId);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }


}
