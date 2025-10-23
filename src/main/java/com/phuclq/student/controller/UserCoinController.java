package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.User;
import com.phuclq.student.domain.UserCoin;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.dto.UserInfoDTO;
import com.phuclq.student.dto.UserPaymentInfor;
import com.phuclq.student.service.EmailSenderService;
import com.phuclq.student.service.OtpService;
import com.phuclq.student.service.UserCoinService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserCoinController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailSenderService emailSenderService;

    @SuppressWarnings("rawtypes")
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("/manager/withdraw")
    public ResponseEntity<?> withdrawMoney() throws Exception {
        Double coin = 0d;
        UserDTO user = userService.getUserResultLogin();
        UserCoin userCoin = userCoinService.getUserCoin(user.getId());
        if (coin <= userCoin.getTotalCoin()) {
            boolean isGenerator = otpService.runGenerateAndSentOtpCode(user);
            if (!isGenerator) {
                throw new Exception("Send otp error!");
            }
        } else {
            throw new Exception("Enough coin to withdraw!");
        }
        return new ResponseEntity<>(coin, HttpStatus.OK);
    }

    @GetMapping("/manager/verify-otp")
    public ResponseEntity<?> verityOtp(@RequestParam String otp, @RequestParam Double coin) throws Exception {
        UserDTO user = userService.getUserResultLogin();
        boolean isProcess = otpService.validateOTP(user.getId().toString(), Integer.parseInt(otp));
        if (!isProcess) {
            throw new Exception("Otp wrong!");
        }
        userCoinService.calculateCoin(user.getId(), coin);
        return new ResponseEntity<>("Otp success!", HttpStatus.OK);
    }

    @Scheduled(cron = "0 15 17 * * *", zone = "Asia/Bangkok")
    public void sendMail() {
        List<UserInfoDTO> userInfoDTOs = new ArrayList<UserInfoDTO>();
        userInfoDTOs.add(new UserInfoDTO("huong", "23/12/1993", "huongit1223@gmail.com"));
        userInfoDTOs.add(new UserInfoDTO("ha", "23/12/1993", "mupmipmup93@gmail.com"));
        boolean result = emailSenderService.sendEmailsAuto(userInfoDTOs);
        System.out.println(result);
    }

    @GetMapping("/get-user-coin")
    public ResponseEntity<?> getUserCoin() {
        User user = userService.getUserLogin();
        UserPaymentInfor userPaymentInfor = userCoinService.getUserPaymentInfor(user.getId());
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(userPaymentInfor).getResponse();
    }

}
