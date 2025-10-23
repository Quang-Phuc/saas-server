package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.sell.SellRequest;
import com.phuclq.student.dto.sell.SellResultSearchDto;
import com.phuclq.student.service.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/sell")
public class AdminSellController {

    @Autowired
    private SellService homeService;

    @Autowired
    private RestEntityResponse restEntityRes;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve")
    public ResponseEntity<?> approve(@RequestBody SellRequest request) throws IOException {
        homeService.approve(request.getId());
        String result = "approve success";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SellRequest jobRequest, Pageable pageable) throws IOException {
        SellResultSearchDto jobServiceCV = homeService.searchSell(true, pageable, jobRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


}
