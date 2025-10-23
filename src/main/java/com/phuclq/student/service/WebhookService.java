package com.phuclq.student.service;


import com.phuclq.student.dto.baokim.OrderPaymentResponse;

public interface WebhookService {

    public OrderPaymentResponse markOrderAsPaid(String paymentInfoDto);

}
