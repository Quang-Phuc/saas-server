package com.phuclq.student.domain;


import lombok.Getter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "refresh_token")
public class RefreshToken extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String token;


    @Column(name = "expiry_date")
    private Instant expiryDate;

    public RefreshToken(String email, String token, Instant expiryDate) {
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
        this.setCreatedBy("ADMIN");
        this.setCreatedDate(LocalDateTime.now());

    }

    public RefreshToken() {
    }


}
