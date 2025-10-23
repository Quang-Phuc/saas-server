package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.UserHistoryCoinResultDto;
import com.phuclq.student.service.UserHistoryCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/history-coin")
public class AdminUsersHistoryCoinController {
    @Autowired
    private UserHistoryCoinService coinService;

    @Autowired
    private RestEntityResponse restEntityRes;


    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("")
    public ResponseEntity<?> getHistoryTransaction(
            @RequestBody FileHomePageRequest historyCoinRequest, Pageable pageable) {
        UserHistoryCoinResultDto page = coinService.getHistoryTransaction(historyCoinRequest,
                pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteUser(@PathVariable("Id") Integer id) {
        coinService.deleteHistoryCoin(id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }


}
