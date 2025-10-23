package com.phuclq.student.service.impl;

import com.phuclq.student.domain.*;
import com.phuclq.student.dto.OrderDto;
import com.phuclq.student.dto.baokim.*;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.repository.*;
import com.phuclq.student.request.MySeoTopRequest;
import com.phuclq.student.request.PaymentRequest;
import com.phuclq.student.service.BaoKimService;
import com.phuclq.student.service.PaymentService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.HistoryCoinType;
import com.phuclq.student.types.SeoTopType;
import com.phuclq.student.utils.DateTimeUtils;
import com.phuclq.student.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.phuclq.student.common.Constants.*;
import static com.phuclq.student.utils.DateTimeUtils.yyyyMMddHHmmssSSS;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    public static final String DESCRIPTION_SEND_ORDER = "Nap tien vao tai khoan %s ngay %s";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    OrderPaymentSubServiceImpl orderPaymentSubService;
    @Autowired
    UserHistoryCoinRepository userHistoryCoinRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCoinRepository userCoinRepository;
    @Autowired
    private BaoKimService baoKimService;
    @Autowired
    private UserService userService;
    @Autowired
    private BaoKimProperties baoKimProperties;
    @Autowired
    private UserHistoryCoinRepository repository;
    @Autowired
    private UserCoinBackupRepository userCoinBackupRepository;
    @Autowired
    private FileRepository fileRepository;
    @Value("${money.convert.coin}")
    private int moneyConvertCoin;

    @Value("${money.sale.top.coin}")
    private Double moneySaleTop;

    @Value("${money.sale.top.day}")
    private int moneySaleTopDay;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserCoinBackup findByUserIdBackUp(Integer userId) {
        UserCoinBackup byUserId = userCoinBackupRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoinBackup(userId, 0D);
    }



    @Override
    public BankResponseWrapper getBanks() {
        return baoKimService.getBanks(userService.getUserLogin().getId());
    }

    @Override
    public Object sendOrderPayment(OrderDto dto) {
        log.info("sendOrderPayment{}",dto.toString());
        dto.setBpmId(295);
        User userLogin = userService.getUserLogin();
        if(Objects.isNull(userLogin.getPhone()) || userLogin.getPhone().length() < 10) {
            throw new BusinessHandleException("SS025");
        }

        try {

            OrderSendParamDto orderSendParamDto = new OrderSendParamDto();
            orderSendParamDto.setMrcOrderId(genMrcOrderId(userLogin.getPhone()));
            orderSendParamDto.setTotalAmount(dto.getTotalAmount());

            String description = String.format(DESCRIPTION_SEND_ORDER, userLogin.getPhone(),
                    DateTimeUtils.convertDateTimeToString(LocalDateTime.now(),
                            yyyyMMddHHmmssSSS));
            orderSendParamDto.setDescription(description);

            String callBackDetail = null;
            String callBackCancelUrl = null;
            String urlSuccess = dto.getUrlCallBack().length() > 200 ? dto.getUrlCallBack().substring(0, 200) : dto.getUrlCallBack();
            orderSendParamDto.setUrlSuccess(urlSuccess);

            orderSendParamDto.setMerchantId(
                    !StringUtils.isEmpty(baoKimProperties.getMerchantId()) ? Integer.parseInt(
                            baoKimProperties.getMerchantId()) : 36326);

            callBackDetail = baoKimProperties.getApiDomain() + baoKimProperties.getCallBackDetailUrl();
            callBackCancelUrl = baoKimProperties.getApiDomain() + baoKimProperties.getCallBackCancelUrl();

            orderSendParamDto.setUrlDetail(callBackDetail);
            orderSendParamDto.setWebhooks(baoKimProperties.getWebhookUrl());
            orderSendParamDto.setCustomerEmail(userLogin.getEmail());
            orderSendParamDto.setCustomerPhone(userLogin.getPhone());
            orderSendParamDto.setCustomerName(Objects.nonNull(userLogin.getFullName()) && userLogin.getFullName().split(SPACE).length > 2 ? userLogin.getFullName() : CUSTOMER_NAME + SPACE + userLogin.getPhone());
            orderSendParamDto.setCustomerAddress(userLogin.getAddress());

            orderSendParamDto.setUrlCancel(callBackCancelUrl);
            orderSendParamDto.setBpmId(dto.getBpmId());

            OrderSendResponseWrapper orderSendResponse = null;

            try {
                log.info("send payment info to partner");
                orderSendResponse = orderPaymentSubService.sendOrderPayment(orderSendParamDto,
                        userService.getUserLogin().getId());
                log.info("Send successfully");
            } catch (Exception e) {
                log.error("Send unsuccessfully", e);
                throw new BusinessException("");
            }
            if (orderSendResponse != null && orderSendResponse.getOrderInfo() != null) {
                UserCoin userCoin = findByUserId(userLogin.getId());
                UserHistoryCoin historyCoin = new UserHistoryCoin(Double.valueOf(dto.getTotalAmount()) / moneyConvertCoin,
                        HistoryCoinType.PAY_COIN.getCode(), HistoryCoinType.PAY_COIN.getName(),
                        userLogin.getId(), HistoryCoinType.PAY_COIN.getType(), userCoin.getTotalCoin() + Double.valueOf(dto.getTotalAmount()) / moneyConvertCoin);
                historyCoin.setPaymentOrderId(orderSendResponse.getOrderInfo().getOrderId());
                historyCoin.setMrcOrderId(orderSendParamDto.getMrcOrderId());
                userHistoryCoinRepository.save(historyCoin);

                log.info("update order successfully");
                return new OrderSendDto(orderSendResponse.getOrderInfo());

            } else {
                log.info("orderSendResponse != null && orderSendResponse.getOrderInfo() != null");
                return null;
//				throw new BusinessException("");
            }

        } catch (Exception e) {
            log.error("error", e);
            throw e;
        }
    }

    public UserCoin findByUserId(Integer userId) {
        UserCoin byUserId = userCoinRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoin(userId, 0D);
    }

    private String genMrcOrderId(String phone) {
        String mrcOrderId =
                "HD_" + phone + "_" + (DateTimeUtils.convertDateTimeToString(LocalDateTime.now(),
                        yyyyMMddHHmmssSSS));

        return mrcOrderId;
    }

    @Override
    public Object sendOrderSuccess(PaymentSuccessDto mrcOrderId) {
        orderPaymentSubService.sendOrderSuccess(mrcOrderId);
        return null;
    }

    @Override
    public Object sendOrderDetail(PaymentSuccessDto mrcOrderId) {
        orderPaymentSubService.sendOrderDetail(mrcOrderId);
        return null;
    }

    @Override
    public String seoTop(MySeoTopRequest mySeoTopRequest) {
        User user = userService.getUserLogin();
        boolean passwordDefine = passwordEncoder.matches(mySeoTopRequest.getPassword(), user.getPassword());
        if(passwordDefine &&   userCoinRepository.findByUserId(user.getId()).getTotalCoin() >= moneySaleTop) {
            if (mySeoTopRequest.getType().equals(SeoTopType.SEO_TOP_FILE.getName())) {
                File byId = fileRepository.findById(mySeoTopRequest.getId().intValue()).orElseThrow(() -> new BusinessException(
                        ExceptionUtils.REQUEST_NOT_EXIST));
                Integer userId = userService.getUserLogin().getId();
                if (userId.toString().equals(byId.getCreatedBy())) {
                    byId.setMoneyTop(moneySaleTop);
                    byId.setStartMoneyTop(Objects.nonNull(byId.getStartMoneyTop())?byId.getStartMoneyTop():LocalDateTime.now());
                    byId.setEndMoneyTop(Objects.nonNull(byId.getEndMoneyTop())?byId.getEndMoneyTop().plusDays(moneySaleTopDay):byId.getStartMoneyTop().plusDays(moneySaleTopDay));
                    fileRepository.save(byId);
                    sumMoney(userId, moneySaleTop, SeoTopType.SEO_TOP_FILE.getName());
                    return String.format("%s %s-%s", SEO_DOCUMENT, DateTimeUtils.convertDateTimeToString(byId.getStartMoneyTop(),DateTimeUtils.ddMMyyyy), DateTimeUtils.convertDateTimeToString(byId.getEndMoneyTop(),DateTimeUtils.ddMMyyyy));
                }
            }
            throw new BusinessHandleException("SS022");
        } else {
        throw new BusinessHandleException("SS008");
    }
    }

    public void sumMoney(Integer userId, Double amount, String type) {
        UserCoin user = findByUserId(userId);
        user.setTotalCoin(Objects.nonNull(user.getTotalCoin()) ? user.getTotalCoin() - amount : amount);
        userCoinRepository.save(user);

        UserCoinBackup userCoinDownloadBackup = findByUserIdBackUp(userId);
        userCoinDownloadBackup.setTotalCoin(Objects.nonNull(userCoinDownloadBackup.getTotalCoin()) ? userCoinDownloadBackup.getTotalCoin() - amount : amount);
        userCoinBackupRepository.save(userCoinDownloadBackup);
        historyCoin(userId, amount, Objects.nonNull(user.getTotalCoin()) ? user.getTotalCoin() - amount : amount, type);
    }

    private void historyCoin(Integer userId, Double coin, Double totalCoin, String type) {
        UserHistoryCoin historyCoinDownload = new UserHistoryCoin();
        if (type.equals(SeoTopType.SEO_TOP_FILE.getName())) {
            historyCoinDownload = new UserHistoryCoin(userId, coin, HistoryCoinType.SEO_FILE.getCode(), HistoryCoinType.SEO_FILE.getName(), userId, HistoryCoinType.SEO_FILE.getType(), totalCoin);

        }
        userHistoryCoinRepository.save(historyCoinDownload);

    }

}