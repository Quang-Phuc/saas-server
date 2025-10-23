package com.phuclq.student.dto.noti;


import lombok.Data;

@Data
public class DirectNotification {
    private String target;
    private String title;
    private String message;
}
