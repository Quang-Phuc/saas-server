package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.dto.CaptchaDTO;
import com.phuclq.student.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("/captcha/generate")
    public ResponseEntity<?> generateImage(@RequestBody @Valid CaptchaDTO captchaDTO) throws Exception {
        CaptchaDTO captchaResult = new CaptchaDTO();
        String id = captchaService.generateId();
        captchaResult.setImageCaptcha(captchaService.generateImage(captchaDTO.getClientId(), id, captchaDTO.getTheme()));
        captchaResult.setCaptchaId(id);

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(captchaResult).getResponse();

    }

    @PostMapping("/captcha/valid")
    public ResponseEntity<?> isValidCaptcha(@RequestBody @Valid CaptchaDTO captchaDTO) throws Exception {
        Boolean userInformation = captchaService.isValidCaptcha(captchaDTO.getUserName(), captchaDTO.getCaptchaId(), captchaDTO.getCaptcha());

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(userInformation).getResponse();

    }

}
