package com.phuclq.student.dto.baokim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSimpleDto {

    @JsonProperty("id")
    private Integer paymentOrderId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("mrc_order_id")
    private String paymentMrcOrderId;

    @JsonProperty("txn_id")
    private String txnId;

    @JsonProperty("ref_no")
    private String refNo;

    @JsonProperty("deposit_id")
    private Integer depositId;

    @JsonProperty("merchant_id")
    private Integer merchantId;

    @JsonProperty("total_amount")
    private String totalAmount;

    @JsonProperty("shipping_fee")
    private String shippingFee;

    @JsonProperty("tax_fee")
    private String taxFee;

    @JsonProperty("mrc_fee")
    private String mrcFee;

    @JsonProperty("description")
    private String description;

    @JsonProperty("url_success")
    private String urlSuccess;

    @JsonProperty("url_cancel")
    private String urlCancel;

    @JsonProperty("url_detail")
    private String urlDetail;

    @JsonProperty("stat")
    private String stat;

    @JsonProperty("payment_version")
    private String paymentVersion;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("bpm_id")
    private Integer bpmId;

    @JsonProperty("accept_qrpay")
    private Integer acceptQrpay;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentMrcOrderId() {
        return paymentMrcOrderId;
    }

    public void setPaymentMrcOrderId(String paymentMrcOrderId) {
        this.paymentMrcOrderId = paymentMrcOrderId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Integer getDepositId() {
        return depositId;
    }

    public void setDepositId(Integer depositId) {
        this.depositId = depositId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(String taxFee) {
        this.taxFee = taxFee;
    }

    public String getMrcFee() {
        return mrcFee;
    }

    public void setMrcFee(String mrcFee) {
        this.mrcFee = mrcFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlSuccess() {
        return urlSuccess;
    }

    public void setUrlSuccess(String urlSuccess) {
        this.urlSuccess = urlSuccess;
    }

    public String getUrlCancel() {
        return urlCancel;
    }

    public void setUrlCancel(String urlCancel) {
        this.urlCancel = urlCancel;
    }

    public String getUrlDetail() {
        return urlDetail;
    }

    public void setUrlDetail(String urlDetail) {
        this.urlDetail = urlDetail;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getPaymentVersion() {
        return paymentVersion;
    }

    public void setPaymentVersion(String paymentVersion) {
        this.paymentVersion = paymentVersion;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getBpmId() {
        return bpmId;
    }

    public void setBpmId(Integer bpmId) {
        this.bpmId = bpmId;
    }

    public Integer getAcceptQrpay() {
        return acceptQrpay;
    }

    public void setAcceptQrpay(Integer acceptQrpay) {
        this.acceptQrpay = acceptQrpay;
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