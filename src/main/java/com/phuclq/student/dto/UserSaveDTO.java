package com.phuclq.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveDTO {
    List<RequestFileDTO> files;
    private Integer id;
    private Integer industryId;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private Boolean isDeleted;
    private Integer roleId;
    private Boolean isEnable;
    private Timestamp createdDate;
    private Date birthDay;
    private String fullName;
    private String gender;
    private String address;
    private String introduction;


}
