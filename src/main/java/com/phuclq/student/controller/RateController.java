package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Rate;
import com.phuclq.student.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rate")
public class RateController {
    @Autowired
    private RateService rateService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("")
    public ResponseEntity<?> comment(@RequestBody Rate rate) {
        Rate comment1 = rateService.rate(rate);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(comment1).getResponse();
    }

    @GetMapping("/detail/{idUrl}/{type}")
    public ResponseEntity<?> commentByIdAndType(@PathVariable("idUrl") String id, @PathVariable("type") String type) {
        Map<Double, Long> rateByIdAndType = rateService.rateByIdAndType(id, type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(rateByIdAndType).getResponse();
    }


}
