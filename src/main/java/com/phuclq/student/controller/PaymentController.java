package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.OrderDto;
import com.phuclq.student.dto.baokim.OrderPaymentResponse;
import com.phuclq.student.dto.baokim.PaymentSuccessDto;
import com.phuclq.student.request.MySeoTopRequest;
import com.phuclq.student.request.PaymentRequest;
import com.phuclq.student.service.PaymentService;
import com.phuclq.student.service.WebhookService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WebhookService webhookService;


    @SuppressWarnings("rawtypes")
    @Autowired
    private RestEntityResponse restEntityRes;

    @Value("${student.account.admin}")
    private String value;



    @RequestMapping(value = "/get-banks", method = RequestMethod.GET)
    public ResponseEntity<?> getBanks() {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(paymentService.getBanks()).getResponse();
    }

    @PostMapping(value = "/send-order-payment")
    public ResponseEntity<?> sendOrderPayment(@RequestBody OrderDto dto) {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(paymentService.sendOrderPayment(dto)).getResponse();
    }

    @RequestMapping(value = "/payment-success", method = RequestMethod.GET)
    public ResponseEntity<?> sendOrderSuccess(@ParameterObject PaymentSuccessDto dto) {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(paymentService.sendOrderSuccess(dto)).getResponse();
    }

    @RequestMapping(value = "/send-order-detail", method = RequestMethod.GET)
    public ResponseEntity<?> sendOrderDetail(@RequestParam PaymentSuccessDto mrcOrderId) {
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(paymentService.sendOrderDetail(mrcOrderId)).getResponse();
    }

    @PostMapping(value = "/mark-order-as-paid")
    public OrderPaymentResponse markOrderAsPaid(@RequestBody String orderPaymentInfo) {
        return webhookService.markOrderAsPaid(orderPaymentInfo);
    }

    @PostMapping("/seo")
    public ResponseEntity<?> seoTop(@RequestBody MySeoTopRequest mySeoTopRequest) {
        String result =  paymentService.seoTop(mySeoTopRequest);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }


}
