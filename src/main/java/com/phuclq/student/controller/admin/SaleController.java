package com.phuclq.student.controller.admin;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Sale;
import com.phuclq.student.dto.sale.SaleRequest;
import com.phuclq.student.dto.sale.SaleResultDto;
import com.phuclq.student.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/sale")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private RestEntityResponse restEntityRes;


    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("")
    public ResponseEntity<?> creatOrUpdateJob(@RequestBody SaleRequest contentRequest) throws IOException {
        Long jobServiceCV = saleService.creatOrUpdate(contentRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @PostMapping("/search")
    public ResponseEntity<?> search(Pageable pageable, String search) throws IOException {
        Page<Sale> jobServiceCV = saleService.search(pageable, search);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(jobServiceCV).getResponse();
    }


    @PreAuthorize("hasRole('ADMIN') || hasRole('ADMINSYSTEM')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        saleService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long Id) {
        SaleResultDto industry = saleService.findAllById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(industry).getResponse();
    }

    @GetMapping("/view")
    public ResponseEntity<?> saleView() {
        SaleResultDto industry = saleService.findAllStatus();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(industry).getResponse();
    }


}
