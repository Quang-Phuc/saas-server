package com.phuclq.student.mapper;

import com.phuclq.student.domain.*; // (Import các Entity)
import com.phuclq.student.dto.*; // (Import các DTO)
import com.phuclq.student.types.InterestPaymentType;
import com.phuclq.student.types.InterestTermUnit;
import com.phuclq.student.types.LoanStatus;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class PledgeContractMapper {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public Customer toCustomerEntity(Long storeId,CustomerDto dto) {
        if (dto == null) return null;

        Customer entity = new Customer();

        // Thông tin định danh cơ bản
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(parseDate(dto.getDateOfBirth()));
        entity.setIdentityNumber(dto.getIdentityNumber());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setPermanentAddress(dto.getPermanentAddress());
        entity.setIssueDate(parseDate(dto.getIssueDate()));
        entity.setIssuePlace(dto.getIssuePlace());

        // Thông tin công việc và liên hệ
        entity.setOccupation(dto.getOccupation());
        entity.setWorkplace(dto.getWorkplace());
        entity.setHouseholdRegistration(dto.getHouseholdRegistration());
        entity.setEmail(dto.getEmail());
        entity.setIncomeVndPerMonth(dto.getIncomeVndPerMonth());
        entity.setNote(dto.getNote());

        // Thông tin người liên hệ
        entity.setContactPerson(dto.getContactPerson());
        entity.setContactPhone(dto.getContactPhone());

        // Thông tin vợ/chồng
        entity.setSpouseName(dto.getSpouseName());
        entity.setSpousePhone(dto.getSpousePhone());
        entity.setSpouseOccupation(dto.getSpouseOccupation());

        // Thông tin cha mẹ
        entity.setFatherName(dto.getFatherName());
        entity.setFatherPhone(dto.getFatherPhone());
        entity.setFatherOccupation(dto.getFatherOccupation());
        entity.setMotherName(dto.getMotherName());
        entity.setMotherPhone(dto.getMotherPhone());
        entity.setMotherOccupation(dto.getMotherOccupation());

        // Mã khách hàng & liên kết hệ thống
        entity.setCustomerCode(dto.getCustomerCode());
        entity.setStoreId(storeId);



        return entity;
    }



    public Loan toLoanEntity(Long storeId,LoanDto dto) {
        if (dto == null) return null;
        Loan entity = new Loan();

        entity.setLoanDate(parseDate(dto.getLoanDate()));
        entity.setStoreId(storeId);
        entity.setLoanAmount(dto.getLoanAmount());
        entity.setInterestTermValue(dto.getInterestTermValue());

        // ✅ Xử lý mềm cho enum để tránh lỗi khi nhập sai case
        try {
            entity.setInterestTermUnit(InterestTermUnit.valueOf(dto.getInterestTermUnit().toUpperCase()));
        } catch (Exception e) {
            entity.setInterestTermUnit(null);
        }

        entity.setInterestRateValue(dto.getInterestRateValue());
        entity.setInterestRateUnit(dto.getInterestRateUnit());
        entity.setPaymentCount(dto.getPaymentCount());

        try {
            entity.setInterestPaymentType(InterestPaymentType.valueOf(dto.getInterestPaymentType().toUpperCase()));
        } catch (Exception e) {
            entity.setInterestPaymentType(null);
        }

        entity.setNote(dto.getNote());
        if(Objects.nonNull(dto.getLoanStatus())){
        entity.setStatus(LoanStatus.valueOf(dto.getLoanStatus()));
        }
        entity.setPartnerType(dto.getPartnerType());
        entity.setFollower(dto.getFollower());
        entity.setCustomerSource(dto.getCustomerSource());

        return entity;
    }


    public CollateralAsset toCollateralAssetEntity(Long storeId,CollateralDto dto) {
        if (dto == null) return null;

        CollateralAsset entity = new CollateralAsset();

        // ✅ Gán đầy đủ các trường theo JSON
        entity.setAssetName(dto.getAssetName());
        entity.setAssetType(dto.getAssetType());
        entity.setValuation(dto.getValuation());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setAssetNote(dto.getAssetNote());
        entity.setStoreId(storeId);

        // ✅ Trạng thái mặc định
        entity.setStatus("TrongKho");

        return entity;
    }

    public CollateralAttribute toCollateralAttributeEntity(AssetTypeResponse.AttributeDto dto, Long collateralAssetId) {
        if (dto == null) return null;
        CollateralAttribute entity = new CollateralAttribute();
        entity.setLabel(dto.getLabel());
        entity.setValue(dto.getValue());
        entity.setCollateralAssetId(collateralAssetId);
        return entity;
    }

    public CustomerDto toCustomerDto(Customer entity) {
        if (entity == null) return null;
        CustomerDto dto = new CustomerDto();
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth() != null ? entity.getDateOfBirth().toString() : null);
        dto.setIdentityNumber(entity.getIdentityNumber());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setPermanentAddress(entity.getPermanentAddress());
        dto.setIssueDate(entity.getIssueDate() != null ? entity.getIssueDate().toString() : null);
        dto.setIssuePlace(entity.getIssuePlace());
        dto.setOccupation(entity.getOccupation());
        dto.setWorkplace(entity.getWorkplace());
        dto.setHouseholdRegistration(entity.getHouseholdRegistration());
        dto.setEmail(entity.getEmail());
        dto.setIncomeVndPerMonth(entity.getIncomeVndPerMonth());
        dto.setNote(entity.getNote());
        dto.setContactPerson(entity.getContactPerson());
        dto.setContactPhone(entity.getContactPhone());
        dto.setSpouseName(entity.getSpouseName());
        dto.setSpousePhone(entity.getSpousePhone());
        dto.setSpouseOccupation(entity.getSpouseOccupation());
        dto.setFatherName(entity.getFatherName());
        dto.setFatherPhone(entity.getFatherPhone());
        dto.setFatherOccupation(entity.getFatherOccupation());
        dto.setMotherName(entity.getMotherName());
        dto.setMotherPhone(entity.getMotherPhone());
        dto.setMotherOccupation(entity.getMotherOccupation());
        dto.setCustomerCode(entity.getCustomerCode());
        dto.setIdUrl(entity.getIdUrl());
        return dto;
    }

    public LoanDto toLoanDto(Loan entity) {
        if (entity == null) return null;
        LoanDto dto = new LoanDto();
        dto.setLoanDate(entity.getLoanDate() != null ? entity.getLoanDate().toString() : null);
        dto.setLoanAmount(entity.getLoanAmount());
        dto.setInterestTermValue(entity.getInterestTermValue());
        dto.setInterestTermUnit(entity.getInterestTermUnit() != null ? entity.getInterestTermUnit().name() : null);
        dto.setInterestRateValue(entity.getInterestRateValue());
        dto.setInterestRateUnit(entity.getInterestRateUnit());
        dto.setPaymentCount(entity.getPaymentCount());
        dto.setInterestPaymentType(entity.getInterestPaymentType() != null ? entity.getInterestPaymentType().name() : null);
        dto.setNote(entity.getNote());
//        dto.setLoanStatus(entity.getLoanStatus());
        dto.setPartnerType(entity.getPartnerType());
        dto.setFollower(entity.getFollower());
        dto.setCustomerSource(entity.getCustomerSource());
        return dto;
    }

    public CollateralDto toCollateralDto(CollateralAsset entity) {
        if (entity == null) return null;
        CollateralDto dto = new CollateralDto();
        dto.setAssetName(entity.getAssetName());
        dto.setAssetType(entity.getAssetType());
        dto.setValuation(entity.getValuation());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setAssetNote(entity.getAssetNote());
        dto.setStatus(entity.getStatus());
        dto.setStoreId(entity.getStoreId());
        return dto;
    }

    public AssetTypeResponse.AttributeDto toCollateralAttributeDto(CollateralAttribute entity) {
        if (entity == null) return null;
        AssetTypeResponse.AttributeDto dto = new AssetTypeResponse.AttributeDto();
        dto.setLabel(entity.getLabel());
        dto.setValue(entity.getValue());
        return dto;
    }

    public PaymentScheduleDto toPaymentScheduleDto(PaymentSchedule entity) {
        if (entity == null) return null;
        PaymentScheduleDto dto = new PaymentScheduleDto();
        dto.setPeriodNumber(entity.getPeriodNumber());
        dto.setDueDate(entity.getDueDate() != null ? entity.getDueDate().toString() : null);
        dto.setInterestAmount(entity.getInterestAmount());
        dto.setPrincipalAmount(entity.getPrincipalAmount());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setStatus(entity.getStatus());
        return dto;
    }


}