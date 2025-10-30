package com.phuclq.student.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.phuclq.student.dto.baokim.*;
import com.phuclq.student.exception.BusinessAssert;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.service.JwtEncryptSubService;
import com.phuclq.student.service.RequestHistorySubService;
import com.phuclq.student.types.RequestType;
import com.phuclq.student.types.StatusType;
import com.phuclq.student.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderPaymentSubServiceImpl implements InitializingBean {


    @Autowired
    private BaoKimProperties baoKimProperties;
    @Autowired
    private RequestHistorySubService requestHistorySubService;
    @Autowired
    private JwtEncryptSubService jwtEncryptSubService;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectionPool(
                        new ConnectionPool(10, 4, TimeUnit.MINUTES))
                .build();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(
                httpClient);

        restTemplate = restTemplateBuilder.requestFactory(() -> requestFactory)
                .setReadTimeout(Duration.ofSeconds(60))
                .setConnectTimeout(Duration.ofSeconds(60))
                .build();
    }

    public OrderSendResponseWrapper sendOrderPayment(OrderSendParamDto orderSendParamDto,
                                                     Integer userId) {
        BusinessAssert.notNull(userId, "userId cannot be null");
        BusinessAssert.notNull(orderSendParamDto, "orderSendParamDto cannot be null");

        String jwt = jwtEncryptSubService.createJWT(userId);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(
                        baoKimProperties.getApiOrderSendUrl())
                .queryParam("jwt", jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = null;
        try {
            body = StringUtils.convertObjectToJson(orderSendParamDto);
        } catch (JsonProcessingException e) {
            log.error("Body put fail" + e);
            throw new UnsupportedOperationException(e);
        }

        HttpEntity<String> request = new HttpEntity<String>(body, headers);

        int statusCode = 0;
        long startTimeCallWs = System.currentTimeMillis();
        String requestContent = null;
        String responseContent = null;
        try {
            ResponseEntity<OrderSendResponseWrapper> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.POST,
                    request,
                    OrderSendResponseWrapper.class);

            responseContent = StringUtils.convertObjectToJson(responseEntity.getBody());
            statusCode = responseEntity.getStatusCode().value();

            return responseEntity.getBody();
        } catch (Exception e) {
            responseContent = e.getMessage();
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException clientError = (HttpClientErrorException) e;
                if (clientError.getRawStatusCode() == 400) {
                    throw new BusinessException("");
                }
            }
//            this.emailService.sendMailWarning(RequestType.BAOKIM_SEND_ORDER, startTimeCallWs);
            throw new RuntimeException("Error call api send order", e);
        } finally {
            try {
                requestContent = StringUtils.convertObjectToJson(request);
                requestHistorySubService.saveLog(uriBuilder.toUriString(),
                        requestContent,
                        responseContent,
                        statusCode,
                        RequestType.BAOKIM_SEND_ORDER,
                        startTimeCallWs,
                        userId);
            } catch (Exception ex) {
                log.error("Error convert object to json send order", ex);
            }
        }
    }

    // TODO
    public OrderDetailResponseWrapper getDetailOrderPayment(String paymentOrderId,
                                                            Integer historyId) {
        BusinessAssert.notNull(historyId, "historyId cannot be null");
        BusinessAssert.notNull(paymentOrderId, "paymentOrderId cannot be null");

        String jwt = jwtEncryptSubService.createJWT(historyId);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(
                        baoKimProperties.getApiOrderDetailUrl())
                .queryParam("jwt", jwt)
                .queryParam("id", paymentOrderId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        int statusCode = 0;
        long startTimeCallWs = System.currentTimeMillis();
        String requestContent = null;
        String responseContent = null;

        try {
            ResponseEntity<OrderDetailResponseWrapper> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    OrderDetailResponseWrapper.class);

            responseContent = StringUtils.convertObjectToJson(responseEntity.getBody());
            statusCode = responseEntity.getStatusCode().value();

            return responseEntity.getBody();
        } catch (Exception e) {
            responseContent = e.getMessage();
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException clientError = (HttpClientErrorException) e;
                if (clientError.getRawStatusCode() == 400) {
                    throw new BusinessException("");
                }
            }
//            this.emailService.sendMailWarning(RequestType.BAOKIM_ORDER_DETAIL, startTimeCallWs);
            throw new RuntimeException("Error call api get detail order payment", e);
        } finally {
            try {
                requestContent = StringUtils.convertObjectToJson(request);
                requestHistorySubService.saveLog(uriBuilder.toUriString(),
                        requestContent,
                        responseContent,
                        statusCode,
                        RequestType.BAOKIM_ORDER_DETAIL,
                        startTimeCallWs,
                        historyId);
            } catch (Exception ex) {
                log.error("Error convert object to json get detail order payment", ex);
            }
        }
    }

    public void sendOrderSuccess(PaymentSuccessDto dto) {
        log.info("success baokim");
    }


}
