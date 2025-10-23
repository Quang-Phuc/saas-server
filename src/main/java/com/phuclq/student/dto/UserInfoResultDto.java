package com.phuclq.student.dto;


import lombok.Data;

@Data
public class UserInfoResultDto {

    Integer id;
    private String userName;
    private AttachmentDTO attachmentDTO;

}
