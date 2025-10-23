package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Report;
import com.phuclq.student.dto.ReportDTO;
import com.phuclq.student.dto.ReportResult;
import com.phuclq.student.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private RestEntityResponse restEntityRes;
    @Autowired
    private ReportService reportService;

    @PostMapping("")
    public ResponseEntity<?> report(@RequestBody Report report) {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(reportService.createReport(report)).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody ReportDTO reportDTO, Pageable pageable) {
        ReportResult report = reportService.search(reportDTO, pageable);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(report).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        reportService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @GetMapping("/{Id}")
    public ResponseEntity<?> findById(@PathVariable Long Id) {
        Report RentalHouse = reportService.findAllById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(RentalHouse).getResponse();
    }


}
