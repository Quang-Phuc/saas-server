package com.phuclq.student.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuclq.student.domain.UserCoin;
import com.phuclq.student.domain.UserCoinBackup;
import com.phuclq.student.domain.UserHistoryCoin;
import com.phuclq.student.dto.baokim.BaoKimProperties;
import com.phuclq.student.dto.baokim.OrderPaymentResponse;
import com.phuclq.student.dto.webhook.OrderPaymentInfoDto;
import com.phuclq.student.dto.webhook.OrderSimpleDto;
import com.phuclq.student.dto.webhook.TransactionSimpleDto;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.MbalErrorMessage;
import com.phuclq.student.repository.UserCoinBackupRepository;
import com.phuclq.student.repository.UserCoinRepository;
import com.phuclq.student.repository.UserHistoryCoinRepository;
import com.phuclq.student.service.RequestHistorySubService;
import com.phuclq.student.service.WebhookService;
import com.phuclq.student.types.OrderCallbackStatus;
import com.phuclq.student.types.OrderStatus;
import com.phuclq.student.types.RequestType;
import com.phuclq.student.utils.StringUtils;
import com.phuclq.student.utils.encryption.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Transactional
@Service
public class WebhookServiceImpl implements WebhookService {

    private static final String ISO_TIME_SDF_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper = new CustomObjectMapper();
    @Autowired
    private UserHistoryCoinRepository userHistoryCoinRepository;
    @Autowired
    private UserCoinRepository userCoinRepository;
    @Autowired
    private UserCoinBackupRepository userCoinBackupRepository;
    @Autowired
    private BaoKimProperties baoKimProperties;

    @Value("${money.convert.coin}")
    private int moneyConvertCoin;

    @Autowired
    private RequestHistorySubService requestHistorySubService;

    @Override
    public OrderPaymentResponse markOrderAsPaid(String paymentInfo) {
        logger.info("markOrderAsPaid{}", paymentInfo);
        logger.info("markOrderAsPaid" + paymentInfo);
        String requestContent = null;
        String responseContent = null;
        logger.info("verifySign");
        int statusCode = 0;

        long startTimeCallWs = System.currentTimeMillis();
        OrderPaymentInfoDto paymentInfoDto = verifySign(paymentInfo);

        if (paymentInfoDto == null || paymentInfoDto.getOrder() == null
                || paymentInfoDto.getTransaction() == null) {
            logger.error(
                    "paymentInfoDto == null || paymentInfoDto.getOrder() == null || paymentInfoDto.getTransaction() == null");
            throw new BusinessHandleException("SS010");

        }

        logger.info("checkOrder");
        OrderSimpleDto orderPaymentInfoDto = paymentInfoDto.getOrder();
        TransactionSimpleDto transactionDto = paymentInfoDto.getTransaction();

        logger.info("MrcOrderId = {}", orderPaymentInfoDto.getPaymentMrcOrderId());
        UserHistoryCoin userHistoryCoin = userHistoryCoinRepository.findAllByMrcOrderIdAndTxnIdIsNull(
                orderPaymentInfoDto.getPaymentMrcOrderId());

        if (userHistoryCoin == null) {
            logger.error("userHistoryCoin == null");
            statusCode = 200;
            return new OrderPaymentResponse(MbalErrorMessage.WEBHOOK_OK.getErrorCode(),
                    MbalErrorMessage.WEBHOOK_OK.getErrorMessage());
        }

        try {
            Double totalAmount = new Double(transactionDto.getTotalAmount()) / moneyConvertCoin;
            if (transactionDto.getTotalAmount() == null
                    || totalAmount.compareTo(userHistoryCoin.getCoin())
                    != 0) {
                statusCode = 400;
                throw new BusinessHandleException("SS012");
            }

            Date paidTime = null;

            if (StringUtils.isEmpty(transactionDto.getUpdatedAt())) {
                statusCode = 400;
                throw new BusinessHandleException("SS013");
            }

            try {
                paidTime = new SimpleDateFormat(ISO_TIME_SDF_PATTERN).parse(
                        transactionDto.getUpdatedAt());
            } catch (ParseException e) {
                statusCode = 400;
                throw new BusinessHandleException("SS013");
            }

            userHistoryCoin.setStatus(OrderStatus.PAID.getValue());
            userHistoryCoin.setPaymentTime(paidTime);
            userHistoryCoin.setCallbackStatus(OrderCallbackStatus.NEW.getValue());
            userHistoryCoin.setTxnId(orderPaymentInfoDto.getTxnId());
            userHistoryCoin.setRefNo(orderPaymentInfoDto.getRefNo());

            userHistoryCoinRepository.save(userHistoryCoin);
            UserCoin userCoin = findByUserId(userHistoryCoin.getUserId());
            userCoin.setTotalCoin(Objects.nonNull(userCoin.getTotalCoin()) ? userCoin.getTotalCoin() + Double.parseDouble(transactionDto.getTotalAmount()) / moneyConvertCoin
                    : Double.parseDouble(transactionDto.getTotalAmount()) / moneyConvertCoin);
            userCoinRepository.save(userCoin);

            UserCoinBackup userCoinBackUp = findByUserIdBackUp(userHistoryCoin.getUserId());
            userCoinBackUp.setTotalCoin(Objects.nonNull(userCoinBackUp.getTotalCoin()) ? userCoinBackUp.getTotalCoin() + Double.parseDouble(transactionDto.getTotalAmount()) / moneyConvertCoin
                    : Double.parseDouble(transactionDto.getTotalAmount()) / moneyConvertCoin);
            userCoinBackupRepository.save(userCoinBackUp);
            statusCode = 200;
            OrderPaymentResponse orderPaymentResponse = new OrderPaymentResponse(MbalErrorMessage.WEBHOOK_OK.getErrorCode(),
                    MbalErrorMessage.WEBHOOK_OK.getErrorMessage());
            logger.info("done markOrderAsPaid= {}", orderPaymentResponse);
            return new OrderPaymentResponse(MbalErrorMessage.WEBHOOK_OK.getErrorCode(),
                    MbalErrorMessage.WEBHOOK_OK.getErrorMessage());

        } finally {
            try {
                requestContent = StringUtils.convertObjectToJson(paymentInfo);
                requestHistorySubService.saveLog("/mark-order-as-paid", requestContent, responseContent,
                        statusCode, RequestType.MARK_ORDER_AS_PAID, startTimeCallWs, userHistoryCoin.getId());
            } catch (Exception ex) {
                logger.error("Error convert object to json get bank list", ex);
            }
        }
    }

    public UserCoin findByUserId(Integer userId) {
        UserCoin byUserId = userCoinRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoin(userId, 0D);
    }

    public UserCoinBackup findByUserIdBackUp(Integer userId) {
        UserCoinBackup byUserId = userCoinBackupRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoinBackup(userId, 0D);
    }

    private OrderPaymentInfoDto verifySign(String paymentInfo) {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> data;
        try {
            data = objectMapper.readValue(paymentInfo, typeRef);
        } catch (JsonProcessingException e) {
            // Invalid JSON
            throw new BusinessHandleException("SS010");
        }

        // Remove sign from data
        String sign = (String) data.remove("sign");

        // Compute our signature
        String ourSign;
        try {
            String json = objectMapper.writeValueAsString(data);
            ourSign = HmacUtils.encodeSha256ToHex(json, baoKimProperties.getJwtSecret());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fail to serialize data to JSON", e);
        }

//        if (!StringUtils.isEquals(sign, ourSign)) {
//            throw new BusinessHandleException("SS014");
//        }

        try {
            return objectMapper.readValue(paymentInfo, OrderPaymentInfoDto.class);
        } catch (JsonProcessingException e) {
            // Invalid JSON
            throw new BusinessHandleException("SS010");
        }
    }

    /**
     * @see <a href="https://github.com/FasterXML/jackson-core/issues/507">Jackson feature
     * requirement</a>
     */
    public static class CustomCharacterEscapes extends CharacterEscapes {

        private static final long serialVersionUID = 1L;

        private final SerializedString escapeForSlash = new SerializedString("\\/");
        private final int[] _asciiEscapes;

        public CustomCharacterEscapes() {
            _asciiEscapes = standardAsciiEscapesForJSON();
            _asciiEscapes['/'] = CharacterEscapes.ESCAPE_CUSTOM;
        }

        @Override
        public int[] getEscapeCodesForAscii() {
            return _asciiEscapes;
        }

        @Override
        public SerializableString getEscapeSequence(int i) {
            if (i == '/') {
                return escapeForSlash;
            }
            return null;
        }
    }

    public class CustomObjectMapper extends ObjectMapper {

        private static final long serialVersionUID = 1L;

        public CustomObjectMapper() {
            this.getFactory().setCharacterEscapes(new CustomCharacterEscapes());
        }

    }

}
