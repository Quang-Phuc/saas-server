package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionSimpleDto {

    @JsonProperty("id")
    private Integer paymentOrderId;

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("account_id")
    private Integer accountId;

    @JsonProperty("total_amount")
    private String totalAmount;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("balance")
    private String balance;

    @JsonProperty("opening_freeze_balance")
    private String openingFreezeBalance;

    @JsonProperty("freeze_amount")
    private String freezeAmount;

    @JsonProperty("freeze_balance")
    private String freezeBalance;

    @JsonProperty("ref_no")
    private String refNo;

    @JsonProperty("bank_ref_no")
    private String bankRefNo;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("stat")
    private Integer stat;

    @JsonProperty("description")
    private String description;

    @JsonProperty("fee_amount")
    private String feeAmount;

    @JsonProperty("is_processed")
    private Integer isProcessed;

    @JsonProperty("src_des")
    private String srcDes;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    public Integer getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Integer paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOpeningFreezeBalance() {
        return openingFreezeBalance;
    }

    public void setOpeningFreezeBalance(String openingFreezeBalance) {
        this.openingFreezeBalance = openingFreezeBalance;
    }

    public String getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(String freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(String freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getBankRefNo() {
        return bankRefNo;
    }

    public void setBankRefNo(String bankRefNo) {
        this.bankRefNo = bankRefNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Integer isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getSrcDes() {
        return srcDes;
    }

    public void setSrcDes(String srcDes) {
        this.srcDes = srcDes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}