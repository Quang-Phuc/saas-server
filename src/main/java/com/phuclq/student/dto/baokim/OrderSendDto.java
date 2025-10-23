package com.phuclq.student.dto.baokim;

import java.io.Serializable;

public class OrderSendDto implements Serializable {

    private static final long serialVersionUID = 5968703358446603746L;

    private Integer paymentOrderId;
    private String redirectUrl;
    private String paymentUrl;

    public OrderSendDto(OrderSendSimpleWrapper orderSendSimpleWrapper) {
        super();
        this.paymentOrderId = orderSendSimpleWrapper.getOrderId();
        this.redirectUrl = orderSendSimpleWrapper.getRedirectUrl();
        this.paymentUrl = orderSendSimpleWrapper.getPaymentUrl();
    }

    public Integer getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Integer paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

}
