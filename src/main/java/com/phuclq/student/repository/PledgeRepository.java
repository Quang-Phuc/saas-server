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
import java.util.Optional;

@Repository
public interface PledgeRepository extends JpaRepository<PledgeContract, Long> {

    @Query("SELECT new com.phuclq.student.dto.PledgeContractResponse(" +
            "pc.id, " +
            "l.loanDate, " +
            "l.dueDate, " +
            "c.fullName, " +
            "c.phoneNumber, " +
            "ca.assetCode, " +
            "ca.assetCode, " +
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
            "AND (:assetType IS NULL OR ca.assetCode = :assetType) " +
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
            "c.fullName, c.phoneNumber, c.dateOfBirth, c.identityNumber, c.issueDate, c.issuePlace, " +
            "c.permanentAddress, c.idUrl, " +
            "c.customerCode, c.occupation, c.workplace, c.householdRegistration, c.email, " +
            "c.incomeVndPerMonth, c.contactPerson, c.contactPhone, c.note, " +
            "c.spouseName, c.spousePhone, c.spouseOccupation, " +
            "c.fatherName, c.fatherPhone, c.fatherOccupation, " +
            "c.motherName, c.motherPhone, c.motherOccupation, " +
            "l.assetName, l.assetType, l.loanDate, l.loanAmount, " +
            "l.interestTermValue, l.interestTermUnit, l.interestRateValue, l.interestRateUnit, " +
            "l.interestPaymentType, l.paymentCount, l.note, " +
            "l.loanStatus, l.partnerType, l.follower, l.customerSource, " +
            "ca.valuation, ca.licensePlate, ca.chassisNumber, ca.engineNumber, " +
            "ca.warehouseId, ca.assetCode, ca.assetNote, " +
            "wf.value, wf.valueType, " +
            "sf.value, sf.valueType, " +
            "rf.value, rf.valueType, " +
            "mf.value, mf.valueType) " +
            "FROM PledgeContract pc " +
            "JOIN Customer c ON pc.customerId = c.id " +
            "LEFT JOIN Loan l ON pc.loanId = l.id " +
            "LEFT JOIN CollateralAsset ca ON pc.collateralId = ca.id " +
            "LEFT JOIN FeeDetail wf ON wf.contractId = pc.id AND wf.feeType = 'WAREHOUSE' " +
            "LEFT JOIN FeeDetail sf ON sf.contractId = pc.id AND sf.feeType = 'STORAGE' " +
            "LEFT JOIN FeeDetail rf ON rf.contractId = pc.id AND rf.feeType = 'RISK' " +
            "LEFT JOIN FeeDetail mf ON mf.contractId = pc.id AND mf.feeType = 'MANAGEMENT' " +
            "WHERE pc.id = :id")
    Optional<PledgeContractDetailResponse> findDetailById(@Param("id") Long id);
}
