package com.phuclq.student.service.impl;


import com.phuclq.student.config.JwtTokenUtil;
import com.phuclq.student.domain.RefreshToken;
import com.phuclq.student.domain.TokenFireBase;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.JwtResponse;
import com.phuclq.student.dto.authen.TokenRefreshRequest;
import com.phuclq.student.exception.BusinessHandleException;
import com.phuclq.student.repository.RefreshTokenRepository;
import com.phuclq.student.repository.TokenFireBaseRepository;
import com.phuclq.student.repository.UserRepository;
import com.phuclq.student.security.JwtUtils;
import com.phuclq.student.service.JwtUserDetailsService;
import com.phuclq.student.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    @Autowired
    JwtUtils jwtUtils;
    @Value("${jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TokenFireBaseRepository tokenFireBaseRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String email) {

        RefreshToken refreshToken = new RefreshToken(email, UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDurationMs));
        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            logger.info("getExpiryDate and now: {}{}", token.getExpiryDate(), Instant.now());
            refreshTokenRepository.delete(token);
            throw new BusinessHandleException("SS015");
        }

        return token;
    }

    @Transactional
    @Override
    public int deleteByUserId(Integer userId) {
        return refreshTokenRepository.deleteByEmail(userRepository.findById(userId).get().getEmail());
    }

    @Override
    public JwtResponse refreshToken(String requestRefreshToken) {
        try {
            Optional<RefreshToken> refreshToken = findByToken(requestRefreshToken);
            RefreshToken tokenExpiration = verifyExpiration(refreshToken.get());

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(refreshToken.get().getEmail());
            String token = jwtTokenUtil.generateToken(userDetails);

            return new JwtResponse(token, tokenExpiration.getToken(),
                    tokenExpiration
                            .getEmail(), null, null, null);
        } catch (Exception e) {

            logger.error("getObjectResponseEntity error: {}", e.getMessage());
            throw new BusinessHandleException("CodeExceptionConstants.VERIFY_EXPIRATION");
        }
    }

    public User getUserLoginByToken(HttpServletRequest request) {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User userByUserFaceId = findUserByUserFaceId(username);
            return Objects.nonNull(userByUserFaceId)? userByUserFaceId :findUserByEmail(username);
        }
        throw new BusinessHandleException("SS016");

    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(email);
    }

    public User findUserByUserFaceId(String email) {
        return userRepository.findUserByUserFaceIdAndIsDeletedFalse(email);
    }

    @Override
    public void logout(HttpServletRequest request, TokenRefreshRequest tokenRefreshRequest) {
        Integer id = getUserLoginByToken(request).getId();
        deleteFireBaseToken(tokenRefreshRequest, id);
        deleteByUserId(id);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    public void deleteFireBaseToken(TokenRefreshRequest tokenRefreshRequest, Integer id) {
        if (Objects.nonNull(tokenRefreshRequest.getTokenFireBase())) {
            TokenFireBase allByTokenAndUserId = tokenFireBaseRepository.findAllByTokenAndUserId(tokenRefreshRequest.getTokenFireBase(), id);
            if (Objects.nonNull(allByTokenAndUserId)) {
                tokenFireBaseRepository.delete(allByTokenAndUserId);

            }
        }
    }
}
