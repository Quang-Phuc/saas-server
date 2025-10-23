package com.phuclq.student.dto;

import lombok.Data;

@Data
public class CaptchaDTO {
    String userName;
    String captchaId;
    String theme;
    String secureKe;
    String captcha;
    String imageCaptcha;
    String clientId;
}

