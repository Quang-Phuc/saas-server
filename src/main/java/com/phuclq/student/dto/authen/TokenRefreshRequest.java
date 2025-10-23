package com.phuclq.student.dto.authen;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;
    private String tokenFireBase;

}
