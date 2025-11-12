package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.PaymentSchedule;
import com.phuclq.student.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interests")
@RequiredArgsConstructor
public class InterestController {


    private final RestEntityResponse restEntityRes;

    private final InterestService interestService;

    @GetMapping("/details")
    public ResponseEntity<?> getInterestDetails(@RequestParam("pledgeId") Long pledgeId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "periodNumber") String sort, @RequestParam(defaultValue = "asc") String order) {
        Page<PaymentSchedule> details = interestService.getDetails(pledgeId, page, size, sort, order);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(details).getResponse();
    }
}
