package com.phuclq.student.service;

import com.phuclq.student.domain.PaymentRequest;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.PaymentDTO;
import com.phuclq.student.dto.PaymentRequestDto;
import com.phuclq.student.dto.PaymentResultDto;

import java.io.IOException;

public interface PaymentRequestService {

    PaymentResultDto searchPayment(FileHomePageRequest request, Boolean admin);

    PaymentDTO findAllById(Integer id) throws IOException;

    PaymentRequest save(PaymentRequestDto paymentRequestDto, Boolean admin) throws IOException;

    void deleteById(Integer id);

    void deleteByIdAdmin(Integer id);


}
