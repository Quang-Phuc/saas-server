package com.phuclq.student.mapper;

import com.phuclq.student.domain.*; // (Import các Entity)
import com.phuclq.student.dto.*; // (Import các DTO)
import com.phuclq.student.types.InterestPaymentType;
import com.phuclq.student.types.InterestTermUnit;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        entity.setContractCode(dto.getContractCode());
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
        entity.setLoanStatus(dto.getLoanStatus());
        entity.setPartnerType(dto.getPartnerType());
        entity.setFollower(dto.getFollower());
        entity.setCustomerSource(dto.getCustomerSource());

        return entity;
    }


    public CollateralAsset toCollateralAssetEntity(CollateralDto dto) {
        if (dto == null) return null;

        CollateralAsset entity = new CollateralAsset();

        // ✅ Gán đầy đủ các trường theo JSON
        entity.setAssetName(dto.getAssetName());
        entity.setAssetType(dto.getAssetType());
        entity.setAssetCode(dto.getAssetCode());
        entity.setValuation(dto.getValuation());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setAssetNote(dto.getAssetNote());

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


}