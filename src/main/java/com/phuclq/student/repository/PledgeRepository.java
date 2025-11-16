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
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PledgeRepository extends JpaRepository<PledgeContract, Long> {

    @Query(
            value =
                    "SELECT " +
                            "pc.id AS id, " +
                            "pc.contract_code AS contractCode, " +
                            "l.loan_date AS loanDate, " +
                            "l.due_date AS dueDate, " +
                            "c.full_name AS customerName, " +
                            "c.phone_number AS phoneNumber, " +
                            "GROUP_CONCAT(DISTINCT ca.asset_name SEPARATOR ', ') AS assetName, " +
                            "l.loan_amount AS loanAmount, " +

                            "COALESCE(ps.totalInterest, 0) AS totalInterest, " +
                            "COALESCE(ps.totalWarehouseFee, 0) AS totalWarehouseFee, " +
                            "COALESCE(fd.totalServiceFee, 0) AS totalServiceFee, " +

                            "(l.loan_amount " +
                            " + COALESCE(ps.totalInterest, 0) " +
                            " + COALESCE(ps.totalWarehouseFee, 0) " +
                            " + COALESCE(fd.totalServiceFee, 0)" +
                            ") AS totalReceivable, " +

                            "COALESCE(paid.totalPaid, 0) AS totalPaid, " +

                            "(l.loan_amount " +
                            " + COALESCE(ps.totalInterest, 0) " +
                            " + COALESCE(ps.totalWarehouseFee, 0) " +
                            " + COALESCE(fd.totalServiceFee, 0) " +
                            " - COALESCE(paid.totalPaid, 0)" +
                            ") AS remainingAmount, " +

                            "CASE " +
                            "   WHEN l.status = 'CLOSED' THEN 'DA_DONG' " +
                            "   WHEN l.due_date < CURDATE() AND ( " +
                            "       (l.loan_amount " +
                            "        + COALESCE(ps.totalInterest, 0) " +
                            "        + COALESCE(ps.totalWarehouseFee, 0) " +
                            "        + COALESCE(fd.totalServiceFee, 0) " +
                            "        - COALESCE(paid.totalPaid, 0)) " +
                            "   ) > 0 THEN 'QUA_HAN' " +
                            "   WHEN ( " +
                            "       l.loan_amount " +
                            "       + COALESCE(ps.totalInterest, 0) " +
                            "       + COALESCE(ps.totalWarehouseFee, 0) " +
                            "       + COALESCE(fd.totalServiceFee, 0) " +
                            "       - COALESCE(paid.totalPaid, 0) " +
                            "   ) <= 0 THEN 'DA_TRA_HET' " +
                            "   ELSE 'DANG_VAY' " +
                            "END AS pledgeStatus, " +

                            "l.follower_id AS follower " +

                            "FROM pledge_contracts pc " +
                            "JOIN loans l ON pc.loan_id = l.id " +
                            "JOIN customer c ON pc.customer_id = c.id " +

                            "LEFT JOIN collateral_asset ca ON ca.contract_id = pc.id " +

                            // SUBQUERY CHỐNG NHÂN BẢN LÃI VÀ PHÍ KHO BÃI
                            "LEFT JOIN ( " +
                            "   SELECT contract_id, " +
                            "          SUM(interest_amount) AS totalInterest, " +
                            "          SUM(warehouse_daily_fee) AS totalWarehouseFee " +
                            "   FROM payment_schedule " +
                            "   GROUP BY contract_id " +
                            ") ps ON ps.contract_id = pc.id " +

                            // SUBQUERY CHỐNG NHÂN BẢN FEE DETAILS
                            "LEFT JOIN ( " +
                            "   SELECT fd.contract_id, " +
                            "          SUM( CASE " +
                            "               WHEN fd.value_type = 'AMOUNT' THEN fd.value " +
                            "               WHEN fd.value_type = 'PERCENT' THEN (l2.loan_amount * fd.value / 100) " +
                            "               ELSE 0 END " +
                            "          ) AS totalServiceFee " +
                            "   FROM fee_details fd " +
                            "   JOIN pledge_contracts pc2 ON pc2.id = fd.contract_id " +
                            "   JOIN loans l2 ON l2.id = pc2.loan_id " +
                            "   GROUP BY fd.contract_id " +
                            ") fd ON fd.contract_id = pc.id " +

                            // SUBQUERY CHỐNG NHÂN BẢN totalPaid
                            "LEFT JOIN ( " +
                            "   SELECT contract_id, " +
                            "          SUM(principal_amount + interest_amount + warehouse_daily_fee) AS totalPaid " +
                            "   FROM payment_schedule " +
                            "   WHERE status = 'PAID' " +
                            "   GROUP BY contract_id " +
                            ") paid ON paid.contract_id = pc.id " +

                            "WHERE (:keyword IS NULL OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                            "   OR c.phone_number LIKE CONCAT('%', :keyword, '%') " +
                            "   OR pc.contract_code LIKE CONCAT('%', :keyword, '%')) " +

                            "AND (:status IS NULL OR l.status = :status) " +
                            "AND (:storeId IS NULL OR pc.store_id = :storeId) " +
                            "AND (:fromDate IS NULL OR l.loan_date >= :fromDate) " +
                            "AND (:toDate IS NULL OR l.loan_date <= :toDate) " +
                            "AND (:follower IS NULL OR l.follower_id = :follower) " +

                            "GROUP BY pc.id, pc.contract_code, l.loan_date, l.due_date, " +
                            "c.full_name, c.phone_number, l.loan_amount, l.status, l.follower_id, " +
                            "ps.totalInterest, ps.totalWarehouseFee, fd.totalServiceFee, paid.totalPaid " +

                            "HAVING (:pledgeStatus IS NULL OR " +
                            "   CASE " +
                            "       WHEN :pledgeStatus = 'DANG_VAY' THEN (remainingAmount > 0 AND l.due_date >= CURDATE()) " +
                            "       WHEN :pledgeStatus = 'QUA_HAN' THEN (remainingAmount > 0 AND l.due_date < CURDATE()) " +
                            "       WHEN :pledgeStatus = 'DA_TRA_HET' THEN (remainingAmount <= 0) " +
                            "       WHEN :pledgeStatus = 'DA_DONG' THEN (l.status = 'CLOSED') " +
                            "       ELSE 1=1 " +
                            "   END) " +

                            "ORDER BY l.loan_date DESC",

            countQuery =
                    "SELECT COUNT(DISTINCT pc.id) " +
                            "FROM pledge_contracts pc " +
                            "JOIN loans l ON pc.loan_id = l.id " +
                            "JOIN customer c ON pc.customer_id = c.id " +
                            "LEFT JOIN payment_schedule ps ON ps.contract_id = pc.id " +
                            "LEFT JOIN fee_details fd ON fd.contract_id = pc.id " +
                            "WHERE (:keyword IS NULL OR LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                            "   OR c.phone_number LIKE CONCAT('%', :keyword, '%') " +
                            "   OR pc.contract_code LIKE CONCAT('%', :keyword, '%')) " +
                            "AND (:status IS NULL OR l.status = :status) " +
                            "AND (:storeId IS NULL OR pc.store_id = :storeId) " +
                            "AND (:fromDate IS NULL OR l.loan_date >= :fromDate) " +
                            "AND (:toDate IS NULL OR l.loan_date <= :toDate) " +
                            "AND (:follower IS NULL OR l.follower_id = :follower)",

            nativeQuery = true
    )
    Page<PledgeContractListResponse> searchPledges(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("storeId") Long storeId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
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
