package com.phuclq.student.service.impl;

import com.phuclq.student.dao.PaymentDao;
import com.phuclq.student.domain.*;
import com.phuclq.student.dto.FileHomePageRequest;
import com.phuclq.student.dto.PaymentDTO;
import com.phuclq.student.dto.PaymentRequestDto;
import com.phuclq.student.dto.PaymentResultDto;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.repository.*;
import com.phuclq.student.service.AttachmentService;
import com.phuclq.student.service.EmailSenderService;
import com.phuclq.student.service.PaymentRequestService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
public class PaymentRequestServiceImpl implements PaymentRequestService {

    @Autowired
    PaymentRequestRepository paymentRequestRepository;
    @Autowired
    UserHistoryCoinRepository userHistoryCoinRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private UserCoinRepository userCoinRepository;
    @Autowired
    private UserCoinBackupRepository userCoinBackupRepository;
    @Autowired
    private FCMService fcmService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserRepository userRepository;
    @Value("${student.account.admin}")
    private String value;

    @Override
    public PaymentResultDto searchPayment(FileHomePageRequest request, Boolean admin) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return paymentDao.payment(request, pageable, admin);
    }

    @Override
    public PaymentDTO findAllById(Integer id) throws IOException {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAttachmentDTO(attachmentService.getAttachmentByRequestIdFromS3AndTypes(id,
                Arrays.asList(FileType.PAYMENT_QR.getName(), FileType.PAYMENT_DONE.getName())));
        paymentDTO.setPaymentRequest(paymentRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST)));
        return paymentDTO;
    }

    @Override
    public PaymentRequest save(PaymentRequestDto paymentRequestDto, Boolean admin)
            throws IOException {
        User user = userService.getUserLogin();
        if (!admin) {
            boolean passwordDefine = passwordEncoder.matches(paymentRequestDto.getPassword(),
                    user.getPassword());

            if (!passwordDefine) {
                throw new BusinessHandleException("SS008");

            }
        }
        Integer login = user.getId();

        if (Objects.isNull(paymentRequestDto.getId())) {
            PaymentRequest paymentRequest = new PaymentRequest(login);
            BeanUtils.copyProperties(paymentRequestDto, paymentRequest);
            paymentRequest.setStatus(PaymentType.PAYMENT_PROCESSING.getCode());
            PaymentRequest paymentRequest1 = paymentRequestRepository.save(paymentRequest);
            if (nonNull(paymentRequestDto.getFiles())) {
                UserCoin byUserId = findByUserId(Integer.valueOf(paymentRequest1.getCreatedBy()));
                if (byUserId.getTotalCoin() < paymentRequest1.getCoin()) {
                    throw new BusinessHandleException("SS020");
                }

                List<Attachment> listAttachmentsFromBase64S3 = attachmentService.createListAttachmentsFromBase64S3(
                        paymentRequestDto.getFiles(), paymentRequest1.getId(), login, true);
                listAttachmentsFromBase64S3.forEach(x -> {
                    if (x.getFileType().equals(FileType.PAYMENT_DONE.getName())) {
                        paymentRequest1.setImagePayed(x.getUrl());
                        paymentRequest1.setPaymentDoneDate(LocalDateTime.now());

                    } else {
                        paymentRequest1.setImageQR(x.getUrl());
                    }

                    paymentRequestRepository.save(paymentRequest1);
                });
            }
            User userAdmin = userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(value);
            emailSenderService.sendEmailUser(userAdmin.getEmail(), "Có yêu cầu rút tiền", "Bạn có yêu cầu rút tiền từ " + user.getEmail());
            return paymentRequest;
        } else {
            PaymentRequest byId = paymentRequestRepository.findAllById(paymentRequestDto.getId());
            if (nonNull(paymentRequestDto.getAccountName())) {
                byId.setAccountName(paymentRequestDto.getAccountName());
            }
            if (nonNull(paymentRequestDto.getAccountNumber())) {
                byId.setAccountNumber(paymentRequestDto.getAccountNumber());
            }
            if (nonNull(paymentRequestDto.getBankId())) {
                byId.setBankId(paymentRequestDto.getBankId());
            }
            if (nonNull(paymentRequestDto.getAccountName())) {
                byId.setAccountName(paymentRequestDto.getAccountName());
            }
            if (nonNull(paymentRequestDto.getBankLogo())) {
                byId.setBankLogo(paymentRequestDto.getBankLogo());
            }
            if (nonNull(paymentRequestDto.getBankName())) {
                byId.setBankName(paymentRequestDto.getBankName());
            }
            if (nonNull(paymentRequestDto.getBankShortName())) {
                byId.setBankShortName(paymentRequestDto.getBankShortName());
            }
            if (nonNull(paymentRequestDto.getName())) {
                byId.setName(paymentRequestDto.getName());
            }
            if (nonNull(paymentRequestDto.getCoin())) {
                byId.setCoin(paymentRequestDto.getCoin());
            }
            if (nonNull(paymentRequestDto.getMessage())) {
                byId.setMessages(paymentRequestDto.getMessage());
                fcmService.tokenFireBase(NotificationType.PAYMENT_COMMENT.getTitle(),
                        NotificationType.PAYMENT_COMMENT.getMessage(), new Notification(NotificationType.PAYMENT_COMMENT.getCode(),
                                byId.getCreatedBy(), NotificationType.PAYMENT_COMMENT.getType(), StatusType.DONE.getName(), NotificationType.PAYMENT_COMMENT.getMessage(), NotificationType.PAYMENT_COMMENT.getImageIcon(), NotificationType.PAYMENT_COMMENT.getUrlDetail()));

            }
            if (nonNull(paymentRequestDto.getStatus())) {
                byId.setStatus(paymentRequestDto.getStatus());
                if (paymentRequestDto.getStatus().equals(PaymentType.PAYMENT_REJECT.getCode())) {
                    fcmService.tokenFireBase(NotificationType.PAYMENT_REJECT.getTitle(),
                            NotificationType.PAYMENT_REJECT.getMessage(), new Notification(NotificationType.PAYMENT_REJECT.getCode(),
                                    byId.getCreatedBy(), NotificationType.PAYMENT_REJECT.getType(), StatusType.DONE.getName(), NotificationType.PAYMENT_REJECT.getMessage(), NotificationType.PAYMENT_REJECT.getImageIcon(), NotificationType.PAYMENT_REJECT.getUrlDetail()));
                }
            }
            paymentRequestRepository.save(byId);
            if (nonNull(paymentRequestDto.getFiles())) {
                if (PaymentType.PAYMENT_DONE.getCode().equals(byId.getStatus())) {
                    throw new BusinessHandleException("SS018");
                }
                List<Attachment> listAttachmentsFromBase64S3 = attachmentService.createListAttachmentsFromBase64S3(
                        paymentRequestDto.getFiles(), byId.getId(), login, true);
                listAttachmentsFromBase64S3.forEach(x -> {
                    if (x.getFileType().equals(FileType.PAYMENT_DONE.getName())) {
                        byId.setImagePayed(x.getUrl());
                        byId.setStatus(PaymentType.PAYMENT_DONE.getCode());
                        historyCoin(byId, Integer.parseInt(byId.getCreatedBy()));
                        fcmService.tokenFireBase(NotificationType.PAYMENT_DONE.getTitle(),
                                NotificationType.PAYMENT_DONE.getMessage(), new Notification(NotificationType.PAYMENT_DONE.getCode(),
                                        byId.getCreatedBy(), NotificationType.PAYMENT_DONE.getType(), StatusType.DONE.getName(), NotificationType.PAYMENT_DONE.getMessage(), NotificationType.PAYMENT_DONE.getImageIcon(), NotificationType.PAYMENT_REJECT.getUrlDetail()));
                    } else {
                        byId.setImageQR(x.getUrl());
                    }
                    paymentRequestRepository.save(byId);
                });
            }
        }
        return null;

    }

    public UserCoin findByUserId(Integer userId) {
        UserCoin byUserId = userCoinRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoin(userId, 0D);
    }

    public UserCoinBackup findByUserIdBackUp(Integer userId) {
        UserCoinBackup byUserId = userCoinBackupRepository.findByUserId(userId);
        return Objects.nonNull(byUserId) ? byUserId : new UserCoinBackup(userId, 0D);
    }

    public void historyCoin(PaymentRequest byId, Integer userLogin) {
        UserCoin userCoin = findByUserId(Integer.parseInt(byId.getCreatedBy()));
        UserCoinBackup userCoinBackup = findByUserIdBackUp(
                Integer.parseInt(byId.getCreatedBy()));

        Double totalCoin = nonNull(userCoin.getTotalCoin()) ? userCoin.getTotalCoin() - byId.getCoin()
                : byId.getCoin();
        if (totalCoin < 0) {
            throw new BusinessHandleException("SS017");
        }
        userCoin.setTotalCoin(totalCoin);
        userCoinRepository.save(userCoin);

        Double totalCoinBackup =
                nonNull(userCoinBackup.getTotalCoin()) ? userCoinBackup.getTotalCoin() - byId.getCoin()
                        : byId.getCoin();

        if (totalCoinBackup < 0) {
            throw new BusinessHandleException("SS017");
        }
        userCoinBackup.setTotalCoin(totalCoinBackup);
        userCoinBackupRepository.save(userCoinBackup);

        UserHistoryCoin historyCoin = new UserHistoryCoin(byId.getCoin(),
                HistoryCoinType.PAYMENT_DONE.getCode(), HistoryCoinType.PAYMENT_DONE.getName(), userLogin,
                HistoryCoinType.PAYMENT_DONE.getType(), totalCoin);

        userHistoryCoinRepository.save(historyCoin);
    }


    @Override
    public void deleteById(Integer id) {
        User user = userService.getUserLogin();
        PaymentRequest allByIdAndCreatedBy = paymentRequestRepository.findAllByIdAndCreatedBy(id,
                user.getId().toString());
        paymentRequestRepository.delete(allByIdAndCreatedBy);
    }

    @Override
    public void deleteByIdAdmin(Integer id) {
        paymentRequestRepository.delete(paymentRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST)));

    }
}


