package com.phuclq.student.service;

import com.phuclq.student.domain.ConfirmationToken;
import com.phuclq.student.domain.User;
import com.phuclq.student.repository.ConfirmationTokenRepository;
import com.phuclq.student.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

@Service
@Transactional
public class ConfirmationTokenService {
    @Autowired
    Environment environment;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Value("${serverConfig.host}")
    private String host;

    public void sendEmailRegister(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user.getId());

        confirmationTokenRepository.save(confirmationToken);

        String message = host  + "/terms&condition/" + confirmationToken.getConfirmationToken();
        String sub = "ĐĂNG KÝ TÀI KHOẢN THÀNH CÔNG TẠI SVSHEAR.VN";

        Context context = new Context();
        context.setVariable("name", user.getUserName());
        context.setVariable("activate", message);
        emailSenderService.sendHtmlMessage(user.getEmail(), sub, context,"register.html");
    }


    public User confirmUserAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        User user = userRepository.findById(token.getUserId()).get();
        user.setIsEnable(true);
        userRepository.save(user);
        User saveUser = userRepository.findById(token.getUserId()).get();;
        return saveUser;
    }

    public SimpleMailMessage sendEmailFileHashcode(User user, String mess) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());

        mailMessage.setSubject("Mã code tài liệu!");
        mailMessage.setFrom("quang.phuc.777290596@gmail.com");
        mailMessage.setText(mess);

        return mailMessage;
    }
}
