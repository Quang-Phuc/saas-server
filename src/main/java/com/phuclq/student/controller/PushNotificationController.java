package com.phuclq.student.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.noti.DirectNotification;
import com.phuclq.student.dto.noti.SubscriptionRequest;
import com.phuclq.student.dto.noti.TopicNotification;
import com.phuclq.student.service.impl.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {

    @Autowired
    private FCMService fcmService;
    @Autowired
    private RestEntityResponse restEntityRes;

    //
    @PostMapping("/notification")
    public ResponseEntity<?> sendTargetedNotification(@RequestBody DirectNotification notification) {
        fcmService.sendNotificationToTarget(notification);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();


    }

    @PostMapping("/topic/notification")
    public ResponseEntity<?> sendNotificationToTopic(@RequestBody TopicNotification notification) {
        fcmService.sendNotificationToTopic(notification);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();


    }

    @PostMapping("/topic/subscription")
    public ResponseEntity<?> subscribeToTopic(@RequestBody SubscriptionRequest subscription)
            throws FirebaseMessagingException {
        fcmService.subscribeToTopic(subscription);

        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();


    }


}
