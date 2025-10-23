package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Notification;
import com.phuclq.student.domain.User;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.repository.NotificationRepository;
import com.phuclq.student.repository.UserHistoryCoinRepository;
import com.phuclq.student.service.NotificationService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository paymentRequestRepository;
    @Autowired
    UserHistoryCoinRepository userHistoryCoinRepository;
    @Autowired
    private UserService userService;

    @Override
    public List<Notification> search(Boolean admin) {
        Pageable pageable = PageRequest.of(0, 100);
        Integer userId = userService.getUserLogin().getId();
        return paymentRequestRepository.findAllByAssigneeOrderByIdDesc(
                userId.toString(), pageable).getContent();
    }


    @Override
    public Notification save(Notification notification) {
        User user = userService.getUserLogin();

        Notification byId = paymentRequestRepository.findById(notification.getId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.REQUEST_NOT_EXIST));
        if (!user.getId().toString().equals(byId.getAssignee())) {
            throw new BusinessHandleException("SS007");
        }

        if (nonNull(notification.getIsRead())) {
            byId.setIsRead(notification.getIsRead());
        }
        if (nonNull(notification.getIsView())) {
            byId.setIsView(notification.getIsView());
        }

        paymentRequestRepository.save(byId);

        return null;

    }


    @Override
    public void deleteById(Long id) {
        User user = userService.getUserLogin();
        Notification allByIdAndCreatedBy = paymentRequestRepository.findAllByAssigneeAndAndId(user.getId().toString(), id);
        paymentRequestRepository.delete(allByIdAndCreatedBy);
    }

}


