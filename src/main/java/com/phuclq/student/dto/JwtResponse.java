package com.phuclq.student.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JwtResponse implements Serializable {


    private Long id;
    private String token;
    private String type;
    private String refreshToken;
    private String email;
    private String emailFace;
    private Boolean isFace;
    private List<String> roles;

    public JwtResponse(String token, String refreshToken, String email, List<String> roles,Boolean isFace,String emailFace) {
        this.id = id;
        this.token = token;
        this.refreshToken = refreshToken;
        this.email = email;
        this.emailFace = emailFace;
        this.roles = roles;
        this.type = "Bearer";
        this.isFace = isFace;
    }

    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse() {

    }
}
