package com.phuclq.student.service.impl;

import com.google.firebase.messaging.*;
import com.phuclq.student.domain.Notification;
import com.phuclq.student.domain.TokenFireBase;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.noti.DirectNotification;
import com.phuclq.student.dto.noti.SubscriptionRequest;
import com.phuclq.student.dto.noti.TopicNotification;
import com.phuclq.student.repository.NotificationRepository;
import com.phuclq.student.repository.TokenFireBaseRepository;
import com.phuclq.student.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final UserService userService;
    private final TokenFireBaseRepository tokenFireBaseRepository;
    private final NotificationRepository notificationRepository;

    public void sendNotificationToTarget(DirectNotification notification) {
        val message = Message.builder()
                // Set the configuration for our web notification
                .setWebpushConfig(
                        // Create and pass a WebpushConfig object setting the notification
                        WebpushConfig.builder().setNotification(
                                        // Create and pass a web notification object with the specified title, body, and icon URL
                                        WebpushNotification.builder().setTitle(notification.getTitle())
                                                .setBody(notification.getMessage())
                                                .setIcon("https://svshare.xyz/assets/icons/logo.svg").build())
                                .build())
                // Specify the user to send it to in the form of their token
                .setToken(notification.getTarget()).build();
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    public String tokenFireBase(String title, String message, Notification notification) {
        User userLogin = userService.getUserLogin();
        notificationRepository.save(notification);
        List<TokenFireBase> tokenFireBases = tokenFireBaseRepository.findAllByUserId(userLogin.getId());
        tokenFireBases.forEach(x -> {
            DirectNotification directNotification = new DirectNotification();
            directNotification.setTarget(x.getToken());
            directNotification.setTitle(title);
            directNotification.setMessage(message);
            sendNotificationToTarget(directNotification);
        });
        return null;
    }

    ;

    public void sendNotificationToTopic(TopicNotification notification) {

        val message = Message.builder().setWebpushConfig(WebpushConfig.builder().setNotification(
                        WebpushNotification.builder().setTitle(notification.getTitle())
                                .setBody(notification.getMessage())
                                .setIcon("https://svshare.xyz/assets/icons/logo.svg").build()).build())
                .setTopic(notification.getTopic()).build();

        FirebaseMessaging.getInstance().sendAsync(message);


    }

    public void subscribeToTopic(SubscriptionRequest subscription) throws FirebaseMessagingException {

        FirebaseMessaging.getInstance()
                .subscribeToTopic(Arrays.asList(subscription.getSubscriber()), subscription.getTopic());

    }

}
