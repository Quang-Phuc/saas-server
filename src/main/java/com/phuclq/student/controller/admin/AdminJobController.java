package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.CountResponse;
import com.phuclq.student.dto.JobCVResultDto;
import com.phuclq.student.dto.JobResultDto;
import com.phuclq.student.dto.job.JobRequest;
import com.phuclq.student.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/job")
public class AdminJobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private RestEntityResponse restEntityRes;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve")
    public ResponseEntity<?> approveHome(@RequestBody JobRequest request) throws IOException {
        jobService.approveJob(request.getId(), request.getType());
        String result = "approve success";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody JobRequest jobRequest, Pageable pageable) throws IOException {
        JobResultDto jobServiceCV = jobService.searchAdmin(jobRequest, pageable, false);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cv/search")
    public ResponseEntity<?> searchCV(@RequestBody JobRequest jobRequest, Pageable pageable) throws IOException {
        JobCVResultDto jobServiceCV = jobService.searchAdminCV(jobRequest, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{type}/{Id}")
    public ResponseEntity<?> delete(@PathVariable String type, @PathVariable Long Id) {

        jobService.adminDeleteById(type, Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count")
    public CountResponse getCount(@RequestParam("type") String type) {
        return jobService.getCount(type);
    }

}
