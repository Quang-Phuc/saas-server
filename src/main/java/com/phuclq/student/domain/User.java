package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
public class User extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "industry_id")
    private Integer industryId;

    @Column(name = "USER_FACE_ID")
    private String userFaceId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "is_enable")
    private Boolean isEnable;

    @Column(name = "birth_day")
    private Date birthDay;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "introduction", columnDefinition = "LONGTEXT")
    private String introduction;

    @Column(name = "REFERRAL_CODE")
    private String referralCode;

    @Column(name = "REFERRED_BY")
    private String referredBy;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", industryId=" + industryId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isDeleted=" + isDeleted +
                ", roleId=" + roleId +
                ", isEnable=" + isEnable +
                ", birthDay=" + birthDay +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
