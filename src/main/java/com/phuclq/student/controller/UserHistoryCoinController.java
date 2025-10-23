package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.UserHistoryCoinResultDto;
import com.phuclq.student.dto.UserHistoryCoinResultTotalDto;
import com.phuclq.student.service.UserHistoryCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history-coin")
public class UserHistoryCoinController {
    @Autowired
    private UserHistoryCoinService coinService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("")
    public ResponseEntity<?> getHistoryTransaction(
            @RequestBody FileHomePageRequest historyCoinRequest, Pageable pageable) {
        UserHistoryCoinResultDto page = coinService.getHistoryTransaction(historyCoinRequest,
                pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @GetMapping("/total")
    public ResponseEntity<?> getHistoryTransaction() {
        List<UserHistoryCoinResultTotalDto> page = coinService.getHistoryTransactionTotal();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

}