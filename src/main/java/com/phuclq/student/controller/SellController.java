package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.SellCategory;
import com.phuclq.student.domain.UserHistorySell;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.sell.SellRequest;
import com.phuclq.student.dto.sell.SellResultDto;
import com.phuclq.student.dto.sell.SellResultSearchDto;
import com.phuclq.student.service.SellService;
import com.phuclq.student.types.ActivityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/sell")
public class SellController {
    @Autowired
    private RestEntityResponse restEntityRes;

    @Autowired
    private SellService sellService;

    @PostMapping("")
    public ResponseEntity<?> creatOrUpdateJob(@RequestBody SellRequest request) throws IOException {

        Long jobServiceCV = sellService.creatOrUpdate(request);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(Pageable pageable, String search) throws IOException {
        Page<SellCategory> jobServiceCV = sellService.search(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        sellService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/detail/{idUrl}")
    public ResponseEntity<?> findById( @PathVariable String idUrl) {
        SellResultDto resultDto = sellService.findAllById(null, idUrl);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(resultDto).getResponse();
    }

    @PostMapping("/search-home")
    public ResponseEntity<?> searchSell(@RequestBody SellRequest jobRequest, Pageable pageable) throws IOException {
        jobRequest.setApprove(1);
        SellResultSearchDto resultDto = sellService.searchSell(false, pageable, jobRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(resultDto).getResponse();
    }


    @PostMapping("/user")
    public ResponseEntity<?> mySell(@RequestBody SellRequest request, Pageable pageable) {
        SellResultSearchDto page = sellService.myHome(request, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @GetMapping("/total")
    public ResponseEntity<?> total() {
        TotalMyDTO page = sellService.total();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(page).getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/like")
    public ResponseEntity<String> likeDocument(@PathVariable("id") Long id) {
        String result;
        HttpStatus status;
        UserHistorySell historyFile = sellService.activityHome(id, ActivityConstants.LIKE_SELL);
        if (historyFile == null) {
            status = HttpStatus.ACCEPTED;
            result = "Không thành công";
        } else {
            status = HttpStatus.OK;
            result = "Thành công";
        }
        return restEntityRes.setHttpStatus(status).setDataResponse(result).getResponse();
    }

    @DeleteMapping("/{id}/uncard")
    public ResponseEntity<?> unLike(@PathVariable("id") Long id) {
        sellService.deleteActivityHome(id, ActivityConstants.LIKE_SELL);
        String result = "Bản ghi đã bị loại bỏ khỏi danh sách yêu thích";
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFileHistory(@RequestBody SellRequest request) {
        Page<HistoryFileResult> page = sellService.deleteHistory(request);
        return new ResponseEntity<Page<HistoryFileResult>>(page, HttpStatus.OK);
    }


}
