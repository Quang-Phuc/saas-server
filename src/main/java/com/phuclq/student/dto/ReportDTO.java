package com.phuclq.student.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReportDTO {
    private Integer requestId;

    private String type;

    private BigInteger id;

    private String content;

    private String fullName;

    private String userName;

    private String email;

    private String search;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String createDate;

    private String url;

    public ReportDTO(Object[] obj) {
        this.id = (BigInteger) obj[0];
        this.type = (String) obj[1];
        this.content = (String) obj[2];
        this.fullName = (String) obj[3];
        this.userName = (String) obj[4];
        this.email = (String) obj[5];
        this.createDate = (String) obj[6];
        this.url = (String) obj[7];
        this.requestId = (Integer) obj[8];

    }


}
