package com.phuclq.student.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String emailTakeCoin;
    private String emailPayCoin;
    private Double coin;
}
