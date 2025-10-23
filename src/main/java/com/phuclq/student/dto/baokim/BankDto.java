package com.phuclq.student.dto.baokim;

import java.io.Serializable;

public class BankDto implements Serializable {

    private static final long serialVersionUID = 4991178583443375729L;

    private Integer bpmId;
    private String name;
    private Integer bankId;
    private Integer type;
    private String bankName;
    private String bankShortName;
    private String bankLogo;

    public BankDto(BankSimpleWrapper wrapper) {
        super();
        this.bpmId = wrapper.getId();
        this.name = wrapper.getName();
        this.bankId = wrapper.getBankId();
        this.type = wrapper.getType();
        this.bankName = wrapper.getBankName();
        this.bankShortName = wrapper.getBankShortName();
        this.bankLogo = wrapper.getBankLogo();
    }

    public Integer getBpmId() {
        return bpmId;
    }

    public void setBpmId(Integer bpmId) {
        this.bpmId = bpmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankShortName() {
        return bankShortName;
    }

    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

}
