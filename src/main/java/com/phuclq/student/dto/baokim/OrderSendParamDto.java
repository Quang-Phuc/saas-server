package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderSendParamDto {

    @JsonProperty("mrc_order_id")
    private String mrcOrderId;

    @JsonProperty("merchant_id")
    private Integer merchantId;

    @JsonProperty("total_amount")
    private Integer totalAmount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("url_success")
    private String urlSuccess;

    @JsonProperty("url_detail")
    private String urlDetail;

    @JsonProperty("url_cancel")
    private String urlCancel;

    @JsonProperty("webhooks")
    private String webhooks;

    @JsonProperty("bpm_id")
    private Integer bpmId;

    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("customer_phone")
    private String customerPhone;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_address")
    private String customerAddress;


}
