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

    // feeDetails (flattened)
    private BigDecimal warehouseValue;
    private String warehouseType;
    private BigDecimal storageValue;
    private String storageType;
    private BigDecimal riskValue;
    private String riskType;
    private BigDecimal managementValue;
    private String managementType;

    public PledgeContractDetailResponse(
            Long id,
            String fullName,
            String phoneNumber,
            LocalDate dateOfBirth,
            String identityNumber,
            LocalDate issueDate,
            String issuePlace,
            String permanentAddress,
            String portraitUrl,
            String customerCode,
            String occupation,
            String workplace,
            String householdRegistration,
            String email,
            Long incomeVndPerMonth,
            String contactPerson,
            String contactPhone,
            String note,
            String spouseName,
            String spousePhone,
            String spouseOccupation,
            String fatherName,
            String fatherPhone,
            String fatherOccupation,
            String motherName,
            String motherPhone,
            String motherOccupation,
            String assetName,
            String assetType,
            LocalDate loanDate,
            BigDecimal loanAmount,
            Integer interestTermValue,
            InterestTermUnit interestTermUnit,
            BigDecimal interestRateValue,
            String interestRateUnit,
            String interestPaymentType,
            Integer paymentCount,
            String loanNote,
            String loanStatus,
            String partnerType,
            String follower,
            String customerSource,
            Long valuation,
            String licensePlate,
            String chassisNumber,
            String engineNumber,
            Long warehouseId,
            String assetCode,
            String assetNote,
            BigDecimal warehouseValue,
            String warehouseType,
            BigDecimal storageValue,
            String storageType,
            BigDecimal riskValue,
            String riskType,
            BigDecimal managementValue,
            String managementType
    ) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.identityNumber = identityNumber;
        this.issueDate = issueDate;
        this.issuePlace = issuePlace;
        this.permanentAddress = permanentAddress;
        this.portraitUrl = portraitUrl;
        this.customerCode = customerCode;
        this.occupation = occupation;
        this.workplace = workplace;
        this.householdRegistration = householdRegistration;
        this.email = email;
        this.incomeVndPerMonth = incomeVndPerMonth;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.note = note;
        this.spouseName = spouseName;
        this.spousePhone = spousePhone;
        this.spouseOccupation = spouseOccupation;
        this.fatherName = fatherName;
        this.fatherPhone = fatherPhone;
        this.fatherOccupation = fatherOccupation;
        this.motherName = motherName;
        this.motherPhone = motherPhone;
        this.motherOccupation = motherOccupation;
        this.assetName = assetName;
        this.assetType = assetType;
        this.loanDate = loanDate;
        this.loanAmount = loanAmount;
        this.interestTermValue = interestTermValue;
        this.interestTermUnit = interestTermUnit;
        this.interestRateValue = interestRateValue;
        this.interestRateUnit = interestRateUnit;
        this.interestPaymentType = interestPaymentType;
        this.paymentCount = paymentCount;
        this.loanNote = loanNote;
        this.loanStatus = loanStatus;
        this.partnerType = partnerType;
        this.follower = follower;
        this.customerSource = customerSource;
        this.valuation = valuation;
        this.licensePlate = licensePlate;
        this.chassisNumber = chassisNumber;
        this.engineNumber = engineNumber;
        this.warehouseId = warehouseId;
        this.assetCode = assetCode;
        this.assetNote = assetNote;

        this.warehouseFee = new FeeDetail(toDouble(warehouseValue), warehouseType);
        this.storageFee = new FeeDetail(toDouble(storageValue), storageType);
        this.riskFee = new FeeDetail(toDouble(riskValue), riskType);
        this.managementFee = new FeeDetail(toDouble(managementValue), managementType);
    }

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
