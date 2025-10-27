package com.phuclq.student.service;

import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.dto.UserInfoDTO2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service("emailSenderService")
@Transactional
public class EmailSenderService {

    private static String TITLE = "TITLE";

    private final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public SimpleMailMessage sendOptCode(UserDTO user, Integer otp) {
        log.debug("Sending opt email to '{}'", user.getEmail());
        if (user.getEmail() == null) {
            log.debug("Email OPT doesn't exist for user '{}'", user.getId());
            return null;
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());

        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(emailFrom);
        mailMessage.setText("OTP of you : " + otp);

        return mailMessage;
    }

    public SimpleMailMessage configEmail(UserInfoDTO2 user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(TITLE);
        mailMessage.setFrom(emailFrom);
        mailMessage.setText(user.getUserName());
        return mailMessage;
    }

    public List<SimpleMailMessage> getMails(List<UserInfoDTO2> userInfoDTO2s) {
        List<SimpleMailMessage> simpleMailMessages = new ArrayList<SimpleMailMessage>();
        userInfoDTO2s.forEach(user -> {
            simpleMailMessages.add(configEmail(user));
        });
        return simpleMailMessages;
    }

    public boolean sendEmailsAuto(List<UserInfoDTO2> userInfoDTO2s) {
        try {
            List<SimpleMailMessage> simpleMailMessages = getMails(userInfoDTO2s);
            simpleMailMessages.forEach(mail -> {
                sendEmail(mail);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public SimpleMailMessage sendEmailUser(String email, String sub, String mess) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);

        mailMessage.setSubject(sub);
        mailMessage.setFrom(emailFrom);
        mailMessage.setText(mess);
        sendEmail(mailMessage);
        return mailMessage;
    }

    public void sendHtmlMessage(String to, String subject, Context context,String template) {

        String htmlContent = templateEngine.process(template, context);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

           javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
}
