package com.phuclq.student.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "TOKEN_FIRE_BASE")
@NoArgsConstructor
public class TokenFireBase extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPIRY_DATE")
    private LocalDateTime expiryDate;

    public TokenFireBase(Integer userId, String token, LocalDateTime expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
        this.setCreatedBy("ADMIN");
        this.setCreatedDate(LocalDateTime.now());
    }
}
