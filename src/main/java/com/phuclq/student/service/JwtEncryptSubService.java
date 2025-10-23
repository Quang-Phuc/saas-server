package com.phuclq.student.service;

import com.phuclq.student.dto.baokim.BaoKimProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtEncryptSubService {

    @Autowired
    private BaoKimProperties baoKimProperties;

    @SuppressWarnings("deprecation")
    public String createJWT(Integer jti) {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        String signingKeyB64 = null;
        try {
            signingKeyB64 = Base64.getEncoder()
                    .encodeToString(baoKimProperties.getJwtSecret().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setIssuedAt(now).setNotBefore(now).setId(jti.toString())
                .setIssuer(baoKimProperties.getJwtKey()).signWith(signatureAlgorithm, signingKeyB64);

        if (baoKimProperties.getJwtExpireDuration() > 0) {
            long expMillis = nowMillis + (baoKimProperties.getJwtExpireDuration() * 1000);
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }
}
