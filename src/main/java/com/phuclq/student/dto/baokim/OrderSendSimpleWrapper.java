package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderSendSimpleWrapper {

    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("redirect_url")
    private String redirectUrl;

    @JsonProperty("payment_url")
    private String paymentUrl;

    @JsonProperty("bank_account")
    private BankAccountWrapper bankAccount;
}
