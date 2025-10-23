package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderSendResponseWrapper {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private Object message;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("data")
    private OrderSendSimpleWrapper orderInfo;


}
