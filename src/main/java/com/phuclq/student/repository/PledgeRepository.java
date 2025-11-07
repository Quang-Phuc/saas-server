package com.phuclq.student.repository;

import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.dto.PledgeContractDetailResponse;
import com.phuclq.student.dto.PledgeContractListResponse;
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

    @Query(value =
            "SELECT " +
                    "pc.id AS id, " +
                    "pc.contract_code AS contractCode, " +
                    "l.loan_date AS loanDate, " +
                    "l.due_date AS dueDate, " +
                    "c.full_name AS customerName, " +
                    "c.phone_number AS phoneNumber, " +
                    "GROUP_CONCAT(DISTINCT ca.asset_name SEPARATOR ', ') AS assetName, " +
                    "l.loan_amount AS loanAmount, " +
                    "IFNULL(SUM(CASE WHEN ps.status = 'PAID' THEN ps.principal_amount + ps.interest_amount ELSE 0 END), 0) AS totalPaid, " +
                    "(l.loan_amount - IFNULL(SUM(CASE WHEN ps.status = 'PAID' THEN ps.principal_amount ELSE 0 END), 0)) AS remainingPrincipal, " +
                    "l.loan_status AS status, " +
                    "pc.follower AS follower, " +
                    "pc.pledge_status AS pledgeStatus " +
                    "FROM pledge_contract pc " +
                    "JOIN loan l ON pc.loan_id = l.id " +
                    "JOIN customer c ON pc.customer_id = c.id " +
                    "LEFT JOIN collateral_asset ca ON ca.contract_id = pc.id " +
                    "LEFT JOIN payment_schedule ps ON ps.contract_id = pc.id " +
                    "WHERE (:keyword IS NULL OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "   OR c.phone_number LIKE CONCAT('%', :keyword, '%') " +
                    "   OR pc.contract_code LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (:status IS NULL OR l.loan_status = :status) " +
                    "AND (:storeId IS NULL OR pc.store_id = :storeId) " +
                    "AND (:fromDate IS NULL OR l.loan_date >= :fromDate) " +
                    "AND (:toDate IS NULL OR l.loan_date <= :toDate) " +
                    "AND (:follower IS NULL OR pc.follower = :follower) " +
                    "AND (:pledgeStatus IS NULL OR pc.pledge_status = :pledgeStatus) " +
                    "GROUP BY pc.id, pc.contract_code, l.loan_date, l.due_date, c.full_name, c.phone_number, " +
                    "l.loan_amount, l.loan_status, pc.follower, pc.pledge_status " +
                    "ORDER BY l.loan_date DESC",
            countQuery =
                    "SELECT COUNT(DISTINCT pc.id) " +
                            "FROM pledge_contract pc " +
                            "JOIN loan l ON pc.loan_id = l.id " +
                            "JOIN customer c ON pc.customer_id = c.id " +
                            "LEFT JOIN collateral_asset ca ON ca.contract_id = pc.id " +
                            "WHERE (:keyword IS NULL OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                            "   OR c.phone_number LIKE CONCAT('%', :keyword, '%') " +
                            "   OR pc.contract_code LIKE CONCAT('%', :keyword, '%')) " +
                            "AND (:status IS NULL OR l.loan_status = :status) " +
                            "AND (:storeId IS NULL OR pc.store_id = :storeId) " +
                            "AND (:fromDate IS NULL OR l.loan_date >= :fromDate) " +
                            "AND (:toDate IS NULL OR l.loan_date <= :toDate) " +
                            "AND (:follower IS NULL OR pc.follower = :follower) " +
                            "AND (:pledgeStatus IS NULL OR pc.pledge_status = :pledgeStatus)",
            nativeQuery = true)
    Page<PledgeContractListResponse> searchPledges(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("storeId") Long storeId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("follower") String follower,
            @Param("pledgeStatus") String pledgeStatus,
            Pageable pageable
    );



//    @Query("SELECT new com.phuclq.student.dto.PledgeContractDetailResponse(" +
//            "pc.id, " +
//            "c.fullName, c.phoneNumber, c.dateOfBirth, c.identityNumber, c.issueDate, c.issuePlace, " +
//            "c.permanentAddress, c.idUrl, " +
//            "c.customerCode, c.occupation, c.workplace, c.householdRegistration, c.email, " +
//            "c.incomeVndPerMonth, c.contactPerson, c.contactPhone, c.note, " +
//            "c.spouseName, c.spousePhone, c.spouseOccupation, " +
//            "c.fatherName, c.fatherPhone, c.fatherOccupation, " +
//            "c.motherName, c.motherPhone, c.motherOccupation, " +
//            "l.assetName, l.assetType, l.loanDate, l.loanAmount, " +
//            "l.interestTermValue, l.interestTermUnit, l.interestRateValue, l.interestRateUnit, " +
//            "l.interestPaymentType, l.paymentCount, l.note, " +
//            "l.loanStatus, l.partnerType, l.follower, l.customerSource, " +
//            "ca.valuation, ca.licensePlate, ca.chassisNumber, ca.engineNumber, " +
//            "ca.warehouseId, ca.assetCode, ca.assetNote, " +
//            "wf.value, wf.valueType, " +
//            "sf.value, sf.valueType, " +
//            "rf.value, rf.valueType, " +
//            "mf.value, mf.valueType) " +
//            "FROM PledgeContract pc " +
//            "JOIN Customer c ON pc.customerId = c.id " +
//            "LEFT JOIN Loan l ON pc.loanId = l.id " +
//            "LEFT JOIN CollateralAsset ca ON pc.collateralId = ca.id " +
//            "LEFT JOIN FeeDetail wf ON wf.contractId = pc.id AND wf.feeType = 'WAREHOUSE' " +
//            "LEFT JOIN FeeDetail sf ON sf.contractId = pc.id AND sf.feeType = 'STORAGE' " +
//            "LEFT JOIN FeeDetail rf ON rf.contractId = pc.id AND rf.feeType = 'RISK' " +
//            "LEFT JOIN FeeDetail mf ON mf.contractId = pc.id AND mf.feeType = 'MANAGEMENT' " +
//            "WHERE pc.id = :id")
//    Optional<PledgeContractDetailResponse> findDetailById(@Param("id") Long id);
}
