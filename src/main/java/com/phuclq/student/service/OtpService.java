package com.phuclq.student.service;

import com.phuclq.student.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    private final Logger logger = LoggerFactory.getLogger(OtpService.class);

    @Autowired
    private OtpGenerator otpGenerator;

    @Autowired
    private EmailSenderService emailSenderService;

    public Boolean runGenerateAndSentOtpCode(UserDTO user) {
        String key = String.valueOf(user.getId());
        Integer otp = otpGenerator.generateOTP(key);

        logger.info("Generated OTP: {}", otp);

        if (otp == -1) {
            logger.debug("OTP generator is not working...");
            return false;
        }

        try {
            SimpleMailMessage mailMessage = emailSenderService.sendOptCode(user, otp);
            emailSenderService.sendEmail(mailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("OTP sent mail is not working...");
            return false;
        }
    }

    /**
     * Method for validating provided OTP
     *
     * @param key       - provided key
     * @param otpNumber - provided OTP number
     * @return boolean value (true|false)
     */
    public Boolean validateOTP(String key, Integer otpNumber) {
        Integer cacheOTP = otpGenerator.getOTPByKey(key);

        if (cacheOTP.equals(otpNumber)) {
            otpGenerator.clearOTPFromCache(key);
            return true;
        }

        return false;
    }

}
