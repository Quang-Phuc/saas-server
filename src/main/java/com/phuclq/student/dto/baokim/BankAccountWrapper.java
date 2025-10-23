package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BankAccountWrapper {

    @JsonProperty("acc_name")
    private Integer accName;

    @JsonProperty("acc_no")
    private String accNo;

    @JsonProperty("bank_name")
    private Integer bankName;

    @JsonProperty("branch")
    private String branch;

    @JsonProperty("amount")
    private Integer amount;


}
