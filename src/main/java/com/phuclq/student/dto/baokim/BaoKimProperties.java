package com.phuclq.student.dto.baokim;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("custom.bao-kim")
public class BaoKimProperties {

    private String apiBankListUrl;
    private String apiOrderSendUrl;
    private String apiOrderDetailUrl;
    private String apiDomain;
    private String callBackSuccessUrl;
    private String callBackDetailUrl;
    private String callBackCancelUrl;
    private String callBackViettelPostSuccessUrl;
    private String callBackViettelPostDetailUrl;
    private String callBackViettelPostCancelUrl;
    private String callBackSuccessBuyInsuranceCancerUrl;
    private String callBackCancelBuyInsuranceCancerUrl;
    private String callBackDetailBuyInsuranceCancerUrl;
    private String webhookUrl;
    private String jwtKey;
    private String jwtSecret;
    private long jwtExpireDuration;
    private String merchantId;


}
