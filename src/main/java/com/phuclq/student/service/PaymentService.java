package com.phuclq.student.service;

import com.phuclq.student.dto.OrderDto;
import com.phuclq.student.dto.baokim.BankResponseWrapper;
import com.phuclq.student.dto.baokim.PaymentSuccessDto;
import com.phuclq.student.request.MySeoTopRequest;
import com.phuclq.student.request.PaymentRequest;

public interface PaymentService {


    public BankResponseWrapper getBanks();

    Object sendOrderPayment(OrderDto dto);

    Object sendOrderSuccess(PaymentSuccessDto mrcOrderId);

    Object sendOrderDetail(PaymentSuccessDto mrcOrderId);

    String seoTop(MySeoTopRequest mySeoTopRequest);


}
