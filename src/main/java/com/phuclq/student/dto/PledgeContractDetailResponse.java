package com.phuclq.student.dto;

import com.phuclq.student.types.InterestTermUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
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
    private BigDecimal loanAmount;
    private Integer interestTermValue;
    private InterestTermUnit interestTermUnit;
    private BigDecimal interestRateValue;
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
    private Long warehouseId;
    private String assetCode;
    private String assetNote;
    private BigDecimal warehouseDailyFee;

    // feeDetails (flattened)
    private BigDecimal warehouseValue;
    private String warehouseType;
    private BigDecimal storageValue;
    private String storageType;
    private BigDecimal riskValue;
    private String riskType;
    private BigDecimal managementValue;
    private String managementType;

    private Double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeDetail {
        private Double value;
        private String type;
    }
}
