package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetailResponseWrapper {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private List<String> message;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("data")
    private OrderSimpleDto orderInfo;


}
