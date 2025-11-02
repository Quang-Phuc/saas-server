package com.phuclq.student.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

// Thêm GSON hoặc Jackson
// import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    // Từ portraitInfo
    private String idUrl;

    // Từ customerInfo
    private String fullName;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    // (Thường không cần nếu client gửi đúng "yyyy-MM-dd")
    private String dateOfBirth;
    private String identityNumber;
    private String phoneNumber;
    private String permanentAddress;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String issueDate;
    private String issuePlace;

    // Từ customerExtraInfo
    private String customerCode;
    private String occupation;
    private String workplace;
    private String householdRegistration;
    private String email;
    private BigDecimal incomeVndPerMonth;
    private String note;
    private String contactPerson;
    private String contactPhone;

    // Từ familyInfo
    private String spouseName;
    private String spousePhone;
    private String spouseOccupation;
    private String fatherName;
    private String fatherPhone;
    private String fatherOccupation;
    private String motherName;
    private String motherPhone;
    private String motherOccupation;
}