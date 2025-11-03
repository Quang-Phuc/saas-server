package com.phuclq.student.mapper;

import com.phuclq.student.domain.*; // (Import các Entity)
import com.phuclq.student.dto.*; // (Import các DTO)
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

    public Customer toCustomerEntity(CustomerDto dto) {
        if (dto == null) return null;
        Customer entity = new Customer();
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(parseDate(dto.getDateOfBirth()));
        entity.setIdentityNumber(dto.getIdentityNumber());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setPermanentAddress(dto.getPermanentAddress());
        entity.setIssueDate(parseDate(dto.getIssueDate()));
        entity.setIssuePlace(dto.getIssuePlace());
        entity.setOccupation(dto.getOccupation());
        entity.setEmail(dto.getEmail());
        // (Lưu ý: idUrl (ảnh chân dung) sẽ được gán trong Service sau khi upload)
        return entity;
    }

    public Loan toLoanEntity(LoanDto dto) {
        if (dto == null) return null;
        Loan entity = new Loan();
        entity.setAssetName(dto.getAssetName());
        entity.setAssetType(dto.getAssetType());
        entity.setLoanDate(parseDate(dto.getLoanDate()));
        entity.setContractCode(dto.getContractCode());
        entity.setLoanAmount(dto.getLoanAmount());
        entity.setInterestTermValue(dto.getInterestTermValue());
        entity.setInterestTermUnit(InterestTermUnit.valueOf(dto.getInterestTermUnit().toUpperCase()));
        entity.setInterestRateValue(dto.getInterestRateValue());
        entity.setInterestRateUnit(dto.getInterestRateUnit());
        entity.setPaymentCount(dto.getPaymentCount());
        entity.setInterestPaymentType(dto.getInterestPaymentType());
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
        entity.setValuation(dto.getValuation());
        entity.setLicensePlate(dto.getLicensePlate());
        entity.setChassisNumber(dto.getChassisNumber());
        entity.setEngineNumber(dto.getEngineNumber());
        entity.setWarehouseId(dto.getWarehouseId());
        entity.setAssetCode(dto.getAssetCode());
        entity.setAssetNote(dto.getAssetNote());
        entity.setStatus("TrongKho"); // (Gán trạng thái mặc định)
        return entity;
    }
}