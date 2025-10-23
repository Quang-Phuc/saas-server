package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.UserHistoryJob;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.JobCVResultDto;
import com.phuclq.student.dto.JobResultDto;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.dto.job.JobRequest;
import com.phuclq.student.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/job")
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("cv")
    public ResponseEntity<?> createOrUpdateCV(@RequestBody JobRequest jobRequest) throws IOException {
        Long jobServiceCV = jobService.createOrUpdateCV(jobRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PostMapping("")
    public ResponseEntity<?> creatOrUpdateJob(@RequestBody JobRequest jobRequest) throws IOException {
        Long jobServiceCV = jobService.creatOrUpdateJob(jobRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @GetMapping("level")
    public ResponseEntity<?> level() throws IOException {
        Map jobServiceCV = jobService.level();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody JobRequest jobRequest, Pageable pageable) throws IOException {
        JobResultDto jobServiceCV = jobService.search(jobRequest, pageable, true);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PostMapping("/cv/search")
    public ResponseEntity<?> searchCV(@RequestBody JobRequest jobRequest, Pageable pageable) throws IOException {
        jobRequest.setApprove(1);
        JobCVResultDto jobServiceCV = jobService.searchCV(jobRequest, pageable, true);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


    @PostMapping("/top")
    public ResponseEntity<?> top(@RequestBody JobRequest jobRequest) throws IOException {
        Pageable pageable = PageRequest.of(0, 10);
        HomeResultDto jobServiceCV = jobService.topSame(jobRequest, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


    @CrossOrigin(origins = "*")
    @GetMapping("like/{type}/{id}")
    public ResponseEntity<String> like(@PathVariable("type") String type, @PathVariable("id") Long id) {
        String result;
        HttpStatus status;
        UserHistoryJob historyFile = jobService.activityJobAndCV(id, type, false);
        if (historyFile == null) {
            status = HttpStatus.ACCEPTED;
            result = "Không thành công";
        } else {
            status = HttpStatus.OK;
            result = "Thành công";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @DeleteMapping("/unlike/{type}/{id}")
    public ResponseEntity<?> unLike(@PathVariable("type") String type, @PathVariable("id") Long id) {
        jobService.deleteActivityJob(id, type);
        String result = "Bản ghi đã bị loại bỏ khỏi danh sách yêu thích";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }


    @PostMapping("/user")
    public ResponseEntity<?> myUser(@RequestBody JobRequest request, Pageable pageable) {
        JobCVResultDto page = jobService.sqlJob(request, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @PostMapping("/cv/user")
    public ResponseEntity<?> myUserCV(@RequestBody JobRequest request, Pageable pageable) {
        JobCVResultDto page = jobService.sqlJobCV(request, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @GetMapping("/total")
    public ResponseEntity<?> total(@RequestParam(required = false) String type) {
        TotalMyDTO page = jobService.total(type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @GetMapping("/cv/total")
    public ResponseEntity<?> totalCV() {
        TotalMyDTO page = jobService.totalCV();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @DeleteMapping("/delete-history")
    public ResponseEntity<?> deleteFileHistory(@RequestBody JobRequest request) {
        Page<HistoryFileResult> page = jobService.deleteHistory(request);
        return new ResponseEntity<Page<HistoryFileResult>>(page, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{type}/{Id}")
    public ResponseEntity<?> delete(@PathVariable String type, @PathVariable Long Id) {

        jobService.deleteById(type, Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }


}
