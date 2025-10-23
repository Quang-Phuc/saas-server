package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.home.HomeRequest;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.service.HomeService;
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
@RequestMapping("/api/admin/home")
public class AdminHomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private RestEntityResponse restEntityRes;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve")
    public ResponseEntity<?> approveHome(@RequestBody HomeRequest homeRequest) throws IOException {
        homeService.approveHome(homeRequest.getId());
        String result = "approve success";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody HomeRequest jobRequest, Pageable pageable) throws IOException {
        HomeResultDto jobServiceCV = homeService.searchAdmin(jobRequest, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


}
