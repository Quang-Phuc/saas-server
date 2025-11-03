package com.phuclq.student.repository;

import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.dto.PledgeContractDetailResponse;
import com.phuclq.student.dto.PledgeContractResponse;
import com.phuclq.student.types.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PledgeRepository extends JpaRepository<PledgeContract, Long> {

    @Query("SELECT new com.phuclq.student.dto.PledgeContractResponse(" +
            "pc.id, " +
            "l.loanDate, " +
            "l.dueDate, " +
            "c.fullName, " +
            "c.phoneNumber, " +
            "ca.assetName, " +
            "ca.assetType, " +
            "l.loanAmount, " +
            "CONCAT(l.interestRateValue, ' ', l.interestRateUnit), " +
            "l.remainingPrincipal, " +
            "l.status, " +
            "pc.storeId" +
            ") " +
            "FROM PledgeContract pc " +
            "JOIN Loan l ON pc.loanId = l.id " +
            "JOIN Customer c ON pc.customerId = c.id " +
            "LEFT JOIN CollateralAsset ca ON pc.collateralId = ca.id " +
            "WHERE (:keyword IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR c.phoneNumber LIKE CONCAT('%', :keyword, '%') " +
            "OR l.contractCode LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:status IS NULL OR l.status = :status) " +
            "AND (:storeId IS NULL OR pc.storeId = :storeId) " +
            "AND (:assetType IS NULL OR ca.assetType = :assetType) " +
            "AND (:fromDate IS NULL OR l.loanDate >= :fromDate) " +
            "AND (:toDate IS NULL OR l.loanDate <= :toDate) " +
            "ORDER BY l.loanDate DESC"
    )
    Page<PledgeContractResponse> searchPledges(
            @Param("keyword") String keyword,
            @Param("status") LoanStatus status,
            @Param("storeId") String storeId,
            @Param("assetType") String assetType,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );

    @Query("SELECT new com.phuclq.student.dto.PledgeContractDetailResponse(" +
            "pc.id, " +
            "c.fullName, c.phoneNumber, c.dateOfBirth, c.identityNumber, c.issueDate, c.issuePlace, c.permanentAddress, c.portraitUrl, " +
            "cei.customerCode, cei.occupation, cei.workplace, cei.householdRegistration, cei.email, cei.incomeVndPerMonth, cei.contactPerson, cei.contactPhone, cei.note, " +
            "fi.spouseName, fi.spousePhone, fi.spouseOccupation, fi.fatherName, fi.fatherPhone, fi.fatherOccupation, fi.motherName, fi.motherPhone, fi.motherOccupation, " +
            "l.assetName, l.assetType, l.loanDate, l.loanAmount, l.interestTermValue, l.interestTermUnit, l.interestRateValue, l.interestRateUnit, l.interestPaymentType, l.paymentCount, l.note, " +
            "lei.loanStatus, lei.partnerType, lei.follower, lei.customerSource, " +
            "ci.valuation, ci.licensePlate, ci.chassisNumber, ci.engineNumber, ci.warehouseId, ci.assetCode, ci.assetNote, " +
            "wf.value, wf.type, " +
            "sf.value, sf.type, " +
            "rf.value, rf.type, " +
            "mf.value, mf.type) " +
            "FROM PledgeContract pc " +
            "JOIN Customer c ON pc.customerId = c.id " +
            "LEFT JOIN CustomerExtraInfo cei ON pc.customerExtraInfoId = cei.id " +
            "LEFT JOIN FamilyInfo fi ON pc.familyInfoId = fi.id " +
            "LEFT JOIN Loan l ON pc.loanId = l.id " +
            "LEFT JOIN LoanExtraInfo lei ON pc.loanExtraInfoId = lei.id " +
            "LEFT JOIN CollateralInfo ci ON pc.collateralInfoId = ci.id " +
            "LEFT JOIN FeesInfo wf ON pc.feesInfoId = wf.id AND wf.feeType = 'WAREHOUSE' " +
            "LEFT JOIN FeesInfo sf ON pc.feesInfoId = sf.id AND sf.feeType = 'STORAGE' " +
            "LEFT JOIN FeesInfo rf ON pc.feesInfoId = rf.id AND rf.feeType = 'RISK' " +
            "LEFT JOIN FeesInfo mf ON pc.feesInfoId = mf.id AND mf.feeType = 'MANAGEMENT' " +
            "WHERE pc.id = :id")
    PledgeContractDetailResponse findDetailById(@Param("id") Long id);

}
