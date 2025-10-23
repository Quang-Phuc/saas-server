package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.UserHistoryHome;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.home.HomeRequest;
import com.phuclq.student.dto.job.HomeResultDto;
import com.phuclq.student.dto.webhook.HomeDto;
import com.phuclq.student.service.HomeService;
import com.phuclq.student.types.ActivityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private HomeService homeService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody HomeRequest homeRequest) throws IOException {
        Long id = homeService.create(homeRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(id).getResponse();
    }


    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody HomeRequest jobRequest, Pageable pageable) throws IOException {
        HomeResultDto jobServiceCV = homeService.search(jobRequest, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        homeService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/{idUrl}")
    public ResponseEntity<?> findById(@PathVariable String idUrl) {
        HomeDto RentalHouse = homeService.findAllById(idUrl);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(RentalHouse).getResponse();
    }

    @PostMapping("/top-same")
    public ResponseEntity<?> topSame(@RequestBody HomeRequest jobRequest) throws IOException {
        Pageable pageable = PageRequest.of(0, 10);
        HomeResultDto jobServiceCV = homeService.topSame(jobRequest, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/like")
    public ResponseEntity<String> likeDocument(@PathVariable("id") Long id) {
        String result;
        HttpStatus status;
        UserHistoryHome historyFile = homeService.activityHome(id, ActivityConstants.LIKE_HOME);
        if (historyFile == null) {
            status = HttpStatus.ACCEPTED;
            result = "Không thành công";
        } else {
            status = HttpStatus.OK;
            result = "Thành công";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @DeleteMapping("/{id}/unlike")
    public ResponseEntity<?> unLike(@PathVariable("id") Long id) {
        homeService.deleteActivityHome(id, ActivityConstants.LIKE_HOME);
        String result = "Bản ghi đã bị loại bỏ khỏi danh sách yêu thích";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/card")
    public ResponseEntity<String> cardDocument(@PathVariable("id") Long id) {
        String result;
        HttpStatus status;
        UserHistoryHome historyFile = homeService.activityHome(id, ActivityConstants.CARD_HOME);
        if (historyFile == null) {
            status = HttpStatus.ACCEPTED;
            result = "Bản ghi được thêm vào giỏ hàng ";
        } else {
            status = HttpStatus.OK;
            result = "Bản ghi đã được thêm vào giỏ hàn";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}/uncard")
    public ResponseEntity<?> unCard(@PathVariable("id") Long id) {
        homeService.deleteActivityHome(id, ActivityConstants.CARD_HOME);
        String result = "Bản ghi đã được xóa vào giỏ hàng ";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/user")
    public ResponseEntity<?> myHome(@RequestBody HomeRequest request, Pageable pageable) {
        HomeResultDto page = homeService.myHome(request, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @GetMapping("/total")
    public ResponseEntity<?> total(@RequestParam(required = false) String type) {
        TotalMyDTO page = homeService.total(type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @DeleteMapping("/delete-history")
    public ResponseEntity<?> deleteFileHistory(@RequestBody HomeRequest request) {
        Page<HistoryFileResult> page = homeService.deleteHistory(request);
        return new ResponseEntity<Page<HistoryFileResult>>(page, HttpStatus.OK);
    }


}
