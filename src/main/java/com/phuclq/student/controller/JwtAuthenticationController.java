package com.phuclq.student.controller;

import com.phuclq.student.config.JwtTokenUtil;
import com.phuclq.student.dto.JwtRequest;
import com.phuclq.student.dto.JwtResponse;
import com.phuclq.student.dto.authen.TokenRefreshRequest;
import com.phuclq.student.service.JwtUserDetailsService;
import com.phuclq.student.service.RefreshTokenService;
import com.phuclq.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {
        JwtResponse login = userService.login(authenticationRequest);
        return ok(login);
    }


    @PostMapping("/refresh-token")
    public JwtResponse refreshToken(@Valid @RequestBody TokenRefreshRequest request) {

        return refreshTokenService.refreshToken(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        refreshTokenService.logout(request, tokenRefreshRequest);
    }

}
