package com.phuclq.student.service;

import com.phuclq.student.domain.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> search(Boolean admin);


    Notification save(Notification notification);

    void deleteById(Long id);


}
