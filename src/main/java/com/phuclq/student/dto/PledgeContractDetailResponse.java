package com.phuclq.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PledgeContractDetailResponse {

    private Long id;

    // customerInfo
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String identityNumber;
    private LocalDate issueDate;
    private String issuePlace;
    private String permanentAddress;
    private String portraitUrl;

    // customerExtraInfo
    private String customerCode;
    private String occupation;
    private String workplace;
    private String householdRegistration;
    private String email;
    private Long incomeVndPerMonth;
    private String contactPerson;
    private String contactPhone;
    private String note;

    // familyInfo
    private String spouseName;
    private String spousePhone;
    private String spouseOccupation;
    private String fatherName;
    private String fatherPhone;
    private String fatherOccupation;
    private String motherName;
    private String motherPhone;
    private String motherOccupation;

    // loanInfo
    private String assetName;
    private String assetType;
    private LocalDate loanDate;
    private Long loanAmount;
    private Integer interestTermValue;
    private String interestTermUnit;
    private Double interestRateValue;
    private String interestRateUnit;
    private String interestPaymentType;
    private Integer paymentCount;
    private String loanNote;

    // loanExtraInfo
    private String loanStatus;
    private String partnerType;
    private String follower;
    private String customerSource;

    // feesInfo
    private FeeDetail warehouseFee;
    private FeeDetail storageFee;
    private FeeDetail riskFee;
    private FeeDetail managementFee;

    // collateralInfo
    private Long valuation;
    private String licensePlate;
    private String chassisNumber;
    private String engineNumber;
    private String warehouseId;
    private String assetCode;
    private String assetNote;

    // attachments
    private List<AttachmentDto> attachments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeDetail {
        private Double value;
        private String type; // Nhập Tiền / Nhập %
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String name;
        private String url;
    }
}
