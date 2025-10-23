package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPaymentInfoDto {

    @JsonProperty("order")
    private OrderSimpleDto order;

    @JsonProperty("txn")
    private TransactionSimpleDto transaction;

    @JsonProperty("sign")
    private String sign;

    public OrderSimpleDto getOrder() {
        return order;
    }

    public void setOrder(OrderSimpleDto order) {
        this.order = order;
    }

    public TransactionSimpleDto getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionSimpleDto transaction) {
        this.transaction = transaction;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}