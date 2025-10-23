package com.phuclq.student.service;


import com.phuclq.student.domain.RefreshToken;
import com.phuclq.student.dto.JwtResponse;
import com.phuclq.student.dto.authen.TokenRefreshRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public interface RefreshTokenService {


    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(String userId);


    RefreshToken verifyExpiration(RefreshToken token);

    @Transactional
    int deleteByUserId(Integer userId);

    JwtResponse refreshToken(String requestRefreshToken);

    void logout(HttpServletRequest request, TokenRefreshRequest tokenRefreshRequest);

}
