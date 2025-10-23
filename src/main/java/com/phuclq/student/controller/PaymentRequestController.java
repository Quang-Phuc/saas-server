package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.PaymentRequest;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.PaymentDTO;
import com.phuclq.student.dto.PaymentRequestDto;
import com.phuclq.student.dto.PaymentResultDto;
import com.phuclq.student.service.PaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/payment")
public class PaymentRequestController {

    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("")
    public ResponseEntity<?> search(@RequestBody FileHomePageRequest request) {

        PaymentResultDto result = paymentRequestService.searchPayment(request,
                false);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody PaymentRequestDto paymentRequestDto)
            throws IOException {
        PaymentRequest categorySave = paymentRequestService.save(paymentRequestDto, false);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categorySave)
                .getResponse();
    }


    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Integer Id) {

        paymentRequestService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/{Id}")
    public ResponseEntity<?> findAllById(@PathVariable Integer Id) throws IOException {
        PaymentDTO allById = paymentRequestService.findAllById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(allById).getResponse();
    }


}
