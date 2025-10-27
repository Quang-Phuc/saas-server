package com.phuclq.student.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userID;
    private String name;
    private String email;
    private String password;
    private String tokenFireBase;
    private Boolean isDelete;
    private Boolean type;
    private String userName;

    public JwtRequest(String email, String password, Boolean isDelete) {

        this.email = userID;
        this.email = email;
        this.password = password;
        this.isDelete = isDelete;
    }

}
