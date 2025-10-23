package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankResponseWrapper {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private List<String> message;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("data")
    private List<BankSimpleWrapper> banks;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<BankSimpleWrapper> getBanks() {
        return banks;
    }

    public void setBanks(List<BankSimpleWrapper> banks) {
        this.banks = banks;
    }

}
